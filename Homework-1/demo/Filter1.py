import re
import requests
import pandas as pd
from bs4 import BeautifulSoup


def containsNumber(code):
    return bool(re.search(r'\d', code))

def getCodes():
    url='https://www.mse.mk/mk/stats/symbolhistory/MKPT'
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')

    codes_dropdown=soup.find('select', attrs={'id':'Code'}).find_all('option')
    codes=[]
    for option in codes_dropdown:
        name=option.get('value')
        if containsNumber(name):
            continue
        else:
            codes.append(name)
    codesData=pd.DataFrame(codes)
    codesData.to_csv('codes_file.csv',index=False)

def starting_filter1():
    getCodes()
