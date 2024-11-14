import re
import requests
import pandas as pd
from bs4 import BeautifulSoup


def containsNumber(code):
    return bool(re.search(r'\d', code))


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

    codes = pd.DataFrame(table_data)
    codes = codes.sort_values(by=[0]).reset_index(drop=True)
    codes.to_csv("codes_file.csv", index=False)

    # codes_dropdown=soup.find('select', attrs={'id':'Code'}).find_all('option')
    # codes=[]
    # for option in codes_dropdown:
    #     name=option.get('value')
    #     if containsNumber(name):
    #         continue
    #     else:
    #         codes.append(name)
    # codesData=pd.DataFrame(codes)
    # codesData.to_csv('codes_file.csv',index=False)


def starting_filter1():
    getCodes()
