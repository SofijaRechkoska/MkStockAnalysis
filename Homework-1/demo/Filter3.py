import os
import pandas as pd
from datetime import datetime, timedelta
import requests
from bs4 import BeautifulSoup


def starting_filter3():
    print("Starting Filter3")
    data = pd.read_csv("stock_data.csv")
    codes = data['Code'].unique()
    new_data = []

    for code in codes:
        code_data = data[data['Code'] == code]

        code_data['Date'] = pd.to_datetime(code_data['Date'], format='%d.%m.%Y')

        url = 'https://www.mse.mk/mk/stats/symbolhistory/' + code

        fromDate = code_data['Date'].max()
        print(f"Fetching data for code: {code}, starting from: {fromDate}")

        current_date = datetime.today()

        while current_date > fromDate:
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

    new_data_df = pd.DataFrame(new_data, columns=["Code","Date", "Last trade Price", "Max", "Min",
               "Average Price", "Percentage Change", "Volume", "Turnover in BEST in denars", "Total turnover in denars"])

    updated_data = pd.concat([data, new_data_df]).drop_duplicates(subset=["Code", "Date"])

    updated_data['Date'] = pd.to_datetime(updated_data['Date'], format='%d.%m.%Y')

    updated_data = updated_data.sort_values(by=["Code", "Date"], ascending=[True, False])

    updated_data['Date'] = updated_data['Date'].dt.strftime('%d.%m.%Y')

    updated_data.to_csv("stock_data.csv", index=False)


