import os
import pandas as pd
from datetime import datetime, timedelta
import calendar
import re
import requests
from bs4 import BeautifulSoup
import concurrent.futures
from Filter1 import connect_to_db, getCodes

import sys
sys.path.append('C:/Users/W11/Desktop/Dians/DatabaseFiller/demo')  # Ensure this path is correct
import os
print("Current PYTHONPATH:", os.environ.get('PYTHONPATH'))


CODES_FILE = 'codes_file.csv'
HISTORIC_DATA_FILE = 'stock_data.csv'



def fetch_data_for_code(code):
    print(f"Getting historical data for code: {code}")
    url = 'https://www.mse.mk/mk/stats/symbolhistory/' + code
    toDate = datetime.now()
    historicalData = []

    for i in range(10):
        year = toDate.year
        flag = True
        fromDate = toDate - timedelta(days=366) if calendar.isleap(year) else toDate - timedelta(days=365)

        response = requests.get(url, params={'FromDate': fromDate.strftime('%d.%m.%Y'),
                                             'ToDate': toDate.strftime('%d.%m.%Y')})
        soup = BeautifulSoup(response.text, 'html.parser')

        table = soup.select_one('#resultsTable')
        if table:
            rows = table.select('tr')

            for row in rows:
                cells = row.find_all('td')
                data_row = [code]
                for cell in cells:
                    data_row.append(cell.text if cell else 'None')

                if flag is True:
                    if flag is True:
                        for i in range(len(cells)):
                            cell_text = cells[i].text.strip()
                            if not cell_text:
                                data_row.append('0')
                            else:
                                data_row.append(cell_text)

                        historicalData.append(data_row)
                        flag = False
                elif len(data_row) >= 8 and data_row[7] != '0' and flag is False:
                    historicalData.append(data_row)

        toDate = fromDate

    return historicalData

def getHistoricalData():
    allData = []
    codes = getCodes()

    with concurrent.futures.ThreadPoolExecutor() as executor:
        results = executor.map(fetch_data_for_code, codes)

    for historicalData in results:
        allData.extend(historicalData)

    insert_data_to_db(allData)

def insert_data_to_db(data):
    conn = connect_to_db()
    cursor = conn.cursor()
    cursor.execute("SET datestyle = 'DMY';")


    insert_query = """
        INSERT INTO stock_data (code, date, last_trade_price, max_price, min_price, avg_price, percentage_change, volume, turnover_best, total_turnover)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        ON CONFLICT (code, date) DO UPDATE
        SET last_trade_price = EXCLUDED.last_trade_price,
            max_price = EXCLUDED.max_price,
            min_price = EXCLUDED.min_price,
            avg_price = EXCLUDED.avg_price,
            percentage_change = EXCLUDED.percentage_change,
            volume = EXCLUDED.volume,
            turnover_best = EXCLUDED.turnover_best,
            total_turnover = EXCLUDED.total_turnover;
        """

    for row in data:
        if len(row) == 10:
            cursor.execute(insert_query, row)

    conn.commit()
    cursor.close()
    conn.close()


def starting_filter2():
    print("Starting Filter2...")
    start_time = datetime.now()
    getHistoricalData()
    end_time = datetime.now()
    print("Total time: ", end_time - start_time)



starting_filter2()


