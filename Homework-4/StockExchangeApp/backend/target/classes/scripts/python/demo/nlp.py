import psycopg2
import time
import requests
import os
from dotenv import load_dotenv

from selenium import webdriver
from selenium.webdriver.common.by import By
from threading import Lock
from concurrent.futures import ThreadPoolExecutor, as_completed
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch
from bs4 import BeautifulSoup

# Load multi-class sentiment model
tokenizer = AutoTokenizer.from_pretrained("cardiffnlp/twitter-roberta-base-sentiment")
model = AutoModelForSequenceClassification.from_pretrained("cardiffnlp/twitter-roberta-base-sentiment")
labels = ["NEGATIVE", "NEUTRAL", "POSITIVE"]

lock = Lock()

load_dotenv()

def analyze_sentiment(news_text):
    inputs = tokenizer(news_text, return_tensors="pt", truncation=True, padding=True)
    outputs = model(**inputs)
    probs = torch.nn.functional.softmax(outputs.logits, dim=-1)
    sentiment = labels[torch.argmax(probs)]
    return sentiment

def containsNumber(text):
    return any(char.isdigit() for char in text)

def create_table():
    try:
        connection = psycopg2.connect(
            host=os.getenv('DB_HOST'),
            port=os.getenv('DB_PORT'),
            database=os.getenv('DB_NAME'),
            user=os.getenv('DB_USER'),
            password=os.getenv('DB_PASSWORD')
        )
        cursor = connection.cursor()
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS news_code (
                id SERIAL PRIMARY KEY,
                company_name VARCHAR(255) NOT NULL,
                content TEXT NOT NULL,
                sentiment VARCHAR(50),
                recommendation VARCHAR(50)
            );
        """)
        connection.commit()
        print("Table created or already exists.")
    except psycopg2.Error as e:
        print(f"Error creating table: {e}")
    finally:
        cursor.close()
        connection.close()

def insert_into_database(data):
    try:
        connection = psycopg2.connect(
            host=os.getenv('DB_HOST'),
            port=os.getenv('DB_PORT'),
            database=os.getenv('DB_NAME'),
            user=os.getenv('DB_USER'),
            password=os.getenv('DB_PASSWORD')
        )
        cursor = connection.cursor()

        for item in data:
            cursor.execute("""
                SELECT COUNT(*) FROM news_code WHERE company_name = %s AND content = %s
            """, (item['company_name'], item['content']))
            count = cursor.fetchone()[0]
            if count == 0:
                cursor.execute("""
                    INSERT INTO news_code (company_name, content, sentiment, recommendation)
                    VALUES (%s, %s, %s, %s)
                """, (item['company_name'], item['content'], item['sentiment'], item['recommendation']))

        connection.commit()
    except psycopg2.Error as e:
        print(f"Database error: {e}")
    finally:
        cursor.close()
        connection.close()

def scrape_current_schedule(driver):
    url = 'https://www.mse.mk/en/stats/current-schedule'
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')
    table_data = []

    tables = ['continuousTradingMode-table', 'fixingWith20PercentLimit-table', 'fixingWithoutLimit-table']
    for table in tables:
        table_rows = soup.select(f'#{table} > tbody > tr')
        links_to_process = []

        for i, row in enumerate(table_rows, 1):
            link_selector = f'#{table} > tbody > tr:nth-child({i}) > td:nth-child(1) > a'
            a_tag = soup.select_one(link_selector)
            if a_tag is None:
                break
            if a_tag:
                link_url = a_tag.get('href')
                company_name = a_tag.text.strip()

                if not link_url or containsNumber(company_name):
                    continue
                if not link_url.startswith('http'):
                    link_url = f"https://www.mse.mk{link_url}"

                links_to_process.append((company_name, link_url))

        with ThreadPoolExecutor(max_workers=5) as executor:
            futures = [executor.submit(process_link, driver, company_name, link) for company_name, link in links_to_process]

            for future in as_completed(futures):
                try:
                    content = future.result()
                    if content:
                        table_data.extend(content)
                        insert_into_database(content)
                except Exception as e:
                    print(f"Error processing link: {e}")

    return table_data

def process_link(driver, company_name, link_url):
    data = []
    try:
        lock.acquire()
        driver.get(link_url)
        time.sleep(2)

        def scrape_container(selector):
            container_data = []
            try:
                list_items = driver.find_elements(By.CSS_SELECTOR, f"{selector} > ul > li")
                for j, _ in enumerate(list_items[:4], 1):
                    try:
                        li_element = driver.find_element(By.CSS_SELECTOR, f"{selector} > ul > li:nth-child({j})")
                        a_element = li_element.find_element(By.CSS_SELECTOR, "div > a").get_attribute("href")
                        driver.get(a_element)
                        time.sleep(3)
                        content = driver.find_element(By.CSS_SELECTOR,
                                                      "#root > main > div > div:nth-child(4) > div > div > div").text
                        sentiment = analyze_sentiment(content)
                        recommendation = {
                            "POSITIVE": "Stock will rise",
                            "NEUTRAL": "Hold position",
                            "NEGATIVE": "Stock will fall"
                        }.get(sentiment, "No recommendation")
                        container_data.append({
                            "company_name": company_name,
                            "content": content,
                            "sentiment": sentiment,
                            "recommendation": recommendation
                        })
                        driver.back()
                        time.sleep(2)
                    except Exception as e:
                        print(f"Error in list item {j} for {selector}: {e}")
            except Exception as e:
                print(f"Error extracting from {selector}: {e}")
            return container_data

        data.extend(scrape_container("#mCSB_1_container"))
        data.extend(scrape_container("#mCSB_2_container"))

    except Exception as e:
        print(f"Error navigating to {link_url}: {e}")
    finally:
        lock.release()
        return data

driver = webdriver.Chrome()
create_table()

print("Scraping current schedule data...")
current_schedule_data = scrape_current_schedule(driver)

print("Current schedule data with sentiment:")
for item in current_schedule_data:
    print(f"Company: {item['company_name']}")
    print(f"Content: {item['content']}")
    print(f"Sentiment: {item['sentiment']}")
    print(f"Recommendation: {item['recommendation']}")
    print()

driver.quit()