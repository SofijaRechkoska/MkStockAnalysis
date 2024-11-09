import os
import pandas as pd
from datetime import datetime, timedelta
import calendar
import re
import requests
from bs4 import BeautifulSoup


CODES_FILE= 'codes_file.csv'
HISTORIC_DATA_FILE= 'stock_data.csv'

def getHistoricalData():
    allData = []
    for code in getCodes():
        print(f"Getting historical data for code: {code}")
        url = 'https://www.mse.mk/mk/stats/symbolhistory/' + code
        toDate = datetime.now()
        histroicalData = []

        for i in range(10):
            year = toDate.year
            flag= True
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
                            histroicalData.append(data_row)
                            flag= False
                        elif len(data_row)>=8 and data_row[7]!='0' and flag is False:
                            histroicalData.append(data_row)

            toDate = fromDate

        allData.extend(histroicalData)

    insert_data_to_csv(allData)


def getCodes():
    print("Getting codes...")
    if os.path.exists(CODES_FILE):

        codes_df=pd.read_csv(CODES_FILE)

        return codes_df['0'].tolist()


def insert_data_to_csv(data):
    print("Inserting data...")
    columns = ["Code", "Date", "Last trade Price", "Max", "Min",
               "Average Price", "Percentage Change", "Volume",
               "Turnover in BEST in denars", "Total turnover in denars"]

    new_data = pd.DataFrame(data, columns=columns)

    if os.path.exists(HISTORIC_DATA_FILE):
        existing_data = pd.read_csv(HISTORIC_DATA_FILE)

        combined_data = pd.concat([existing_data, new_data], ignore_index=True)
    else:
        combined_data = new_data

    for col in combined_data.select_dtypes(include=['object']).columns:
        combined_data[col] = combined_data[col].str.strip()

    updated_data = combined_data.drop_duplicates(keep="last")

    updated_data.to_csv(HISTORIC_DATA_FILE, index=False)



def starting_filter2():
    print("Starting Filter2...")
    start_time=datetime.now()
    getHistoricalData()
    end_time=datetime.now()
    print("Total time: ", end_time - start_time)

