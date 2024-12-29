import psycopg2
import re
import requests
from bs4 import BeautifulSoup

from CreatingDB import connect_to_db


def containsNumber(code):
    return bool(re.search(r'\d', code))


def insert_codes_to_db(codes):
    conn = connect_to_db()
    cursor = conn.cursor()

    insert_query = "INSERT INTO codes (code) VALUES (%s) ON CONFLICT (code) DO NOTHING;"

    for code in codes:
        cursor.execute(insert_query, (code,))

    conn.commit()
    cursor.close()
    conn.close()


def getCodes():
    url = 'https://www.mse.mk/en/stats/current-schedule'
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')
    td_elements = soup.select('table tbody td:nth-of-type(1)')

    table_data = []

    for td in td_elements:
        a_tag = td.find('a')
        if a_tag:
            if containsNumber(a_tag.text):
                continue
            table_data.append(a_tag.text.strip())
    print(table_data)
    insert_codes_to_db(table_data)
    print(f"Inserted {len(table_data)} codes into the database.")
    return table_data

getCodes()