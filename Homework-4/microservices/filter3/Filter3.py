from flask import Flask, jsonify
from flask_cors import CORS
from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timedelta
import requests
import traceback
from bs4 import BeautifulSoup
from Filter2 import insert_data_to_db, fetch_data_for_code, getCodes

# Flask app
app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": ["http://localhost:3000", "http://127.0.0.1:3000"]}})

def fetch_stock_data():
    codes = getCodes()
    new_data = []

    with ThreadPoolExecutor(max_workers=10) as executor:
        for code in codes:
            counter = 0
            code_data = fetch_data_for_code(code)
            from_date = max(
                [datetime.strptime(row[1], '%d.%m.%Y') for row in code_data if len(row) > 1],
                default=datetime.today()
            )
            current_date = datetime.today()

            while current_date > from_date:
                if counter == 5:
                    break
                url = f'https://www.mse.mk/mk/stats/symbolhistory/{code}'
                response = requests.get(url, params={
                    'FromDate': from_date.strftime('%d.%m.%Y'),
                    'ToDate': current_date.strftime('%d.%m.%Y')
                })
                if response.status_code == 200:
                    soup = BeautifulSoup(response.text, 'html.parser')

                    table = soup.select_one('#resultsTable')
                    if table:
                        rows = table.select('tr')
                        for row in rows:
                            cells = row.find_all('td')
                            if len(cells) >= 8:
                                data_row = [code] + [cell.text.strip() for cell in cells]
                                if data_row[7] != '0':
                                    print("da")
                                    print(data_row)
                                    new_data.append(data_row)
                            else:
                                if counter == 5:
                                    break
                                counter += 1
                                print(f"Row does not have enough cells: {cells}")
                    else:

                        print(f"No table found for code: {code}")

                current_date -= timedelta(days=1)


    insert_data_to_db(new_data)
    return len(new_data)


@app.route('/filter3', methods=['POST','GET'])
def run_filter3():
    try:
        num_records = fetch_stock_data()
        return jsonify({"message": f"Inserted {num_records} records into the database"}), 200
    except Exception as e:
        error_details = traceback.format_exc()
        print(error_details)
        return jsonify({"error": str(e), "details": error_details}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5002)
