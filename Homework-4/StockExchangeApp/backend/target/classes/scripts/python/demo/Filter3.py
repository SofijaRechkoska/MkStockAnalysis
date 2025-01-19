import os
from concurrent.futures import ThreadPoolExecutor

import pandas as pd
from datetime import datetime, timedelta
import requests
from bs4 import BeautifulSoup
import sys
sys.path.append('backend/src/main/resources/scripts/python/demo')
import os
print("Current PYTHONPATH:", os.environ.get('PYTHONPATH'))


from Filter2 import insert_data_to_db, fetch_data_for_code


def starting_filter3():
    print("Starting Filter3")
    data = fetch_data_for_code()
    codes = {row[0] for row in data}
    new_data = []

    with ThreadPoolExecutor(max_workers=10) as executor:

        for code in codes:
            code_data = [row for row in data if row[0] == code]

            fromDate = max([row[1] for row in code_data])
            print(f"Fetching data for code: {code}, starting from: {fromDate}")

            current_date = datetime.today()

            while current_date > fromDate:
                url = 'https://www.mse.mk/mk/stats/symbolhistory/' + code
                response = requests.get(url, params={'FromDate': fromDate.strftime('%d.%m.%Y'),
                                                     'ToDate': current_date.strftime('%d.%m.%Y')})
                print(f"Status Code: {response.status_code}")

                if response.status_code == 200:
                    soup = BeautifulSoup(response.text, 'html.parser')
                    table = soup.select_one('#resultsTable')

                    if table:
                        rows = table.select('tr')

                        for row in rows:
                            cells = row.find_all('td')
                            data_row = [code]

                            for cell in cells:
                                data_row.append(cell.text if cell else 'None')
                                if len(data_row) >= 8 and data_row[7] != '0':
                                    new_data.append(data_row)

                current_date -= timedelta(days=1)

    insert_data_to_db(new_data)
