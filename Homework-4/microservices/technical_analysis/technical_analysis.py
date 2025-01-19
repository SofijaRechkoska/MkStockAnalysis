import pandas as pd
import psycopg2
from ta.trend import SMAIndicator, EMAIndicator, MACD
from ta.momentum import RSIIndicator, StochasticOscillator
from ta.volume import VolumeWeightedAveragePrice
from flask import Flask, jsonify
from flask_cors import CORS


app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": ["http://localhost:3000", "http://127.0.0.1:3000"]}})

def get_data_from_db():
    connection = psycopg2.connect(
        host="localhost",
        port="8080",
        database="stock-market",
        user="postgres",
        password="Dimitar@123"
    )

    query = "SELECT * FROM public.stock_data"

    try:
        data = pd.read_sql(query, connection)
    except Exception as e:
        print("Error reading data:", e)
        data = pd.DataFrame()

    connection.close()
    print("Number of codes retrieved:", len(data['code'].unique()))
    return data


def process_stock_data():
    data = get_data_from_db()

    if data.empty:
        print("No data found in the database.")
        return

    data['date'] = pd.to_datetime(data['date'], errors='coerce')
    data.set_index('date', inplace=True)

    data['last_trade_price'] = pd.to_numeric(data['last_trade_price'].str.replace(',', ''), errors='coerce')
    data['max_price'] = pd.to_numeric(data['max_price'].str.replace(',', ''), errors='coerce')
    data['min_price'] = pd.to_numeric(data['min_price'].str.replace(',', ''), errors='coerce')
    data['avg_price'] = pd.to_numeric(data['avg_price'].str.replace(',', ''), errors='coerce')
    data['volume'] = pd.to_numeric(data['volume'], errors='coerce')

    data = data.fillna(method='ffill')
    data = data.fillna(method='bfill')

    data = data.sort_index(ascending=False)

    all_timeframes = []
    for code in data['code'].unique():
        code_data = data[data['code'] == code]

        for timeframe, rule in [('daily', '1D'), ('weekly', '1W'), ('monthly', '1M')]:
            resampled_data = code_data.resample(rule).agg({
                'last_trade_price': 'last',
                'max_price': 'max',
                'min_price': 'min',
                'avg_price': 'mean',
                'volume': 'sum',
                'code': 'first'
            })

            resampled_data = calculate_indicators(resampled_data)
            resampled_data = generate_signals(resampled_data)
            resampled_data['timeframe'] = timeframe
            all_timeframes.append(resampled_data)

    combined_data = pd.concat(all_timeframes)
    combined_data.dropna(subset=['code'],inplace=True)

    save_predictions_to_db(combined_data)

    return combined_data


def calculate_indicators(df):
    df['SMA_20'] = SMAIndicator(close=df['last_trade_price'], window=20).sma_indicator()
    df['SMA_50'] = SMAIndicator(close=df['last_trade_price'], window=50).sma_indicator()
    df['EMA_12'] = EMAIndicator(close=df['last_trade_price'], window=12).ema_indicator()
    df['EMA_26'] = EMAIndicator(close=df['last_trade_price'], window=26).ema_indicator()
    df['RSI'] = RSIIndicator(close=df['last_trade_price'], window=14).rsi()
    df['MACD'] = MACD(close=df['last_trade_price'], window_slow=26, window_fast=12, window_sign=9).macd()
    df['Stochastic'] = StochasticOscillator(
        high=df['max_price'],
        low=df['min_price'],
        close=df['last_trade_price'],
        window=14
    ).stoch()
    df['VWMA'] = VolumeWeightedAveragePrice(
        high=df['max_price'],
        low=df['min_price'],
        close=df['last_trade_price'],
        volume=df['volume']
    ).volume_weighted_average_price()
    return df


def generate_signals(df):
    df['Signal'] = 'Hold'

    df.loc[df['RSI'] < 30, 'Signal'] = 'Buy'
    df.loc[df['RSI'] > 70, 'Signal'] = 'Sell'

    df.loc[df['SMA_20'] > df['SMA_50'], 'Signal'] = 'Buy'
    df.loc[df['SMA_20'] < df['SMA_50'], 'Signal'] = 'Sell'

    df.loc[df['MACD'] > 0, 'Signal'] = 'Buy'
    df.loc[df['MACD'] < 0, 'Signal'] = 'Sell'

    return df


def save_predictions_to_db(data):
    connection = psycopg2.connect(
        host="localhost",
        port="8080",
        database="stock-market",
        user="postgres",
        password="Dimitar@123"
    )

    create_table_query = """
    CREATE TABLE IF NOT EXISTS predictions (
        date TEXT,
        code TEXT,
        signal TEXT,
        last_trade_price REAL,
        sma_20 REAL,
        sma_50 REAL,
        ema_12 REAL,
        ema_26 REAL,
        rsi REAL,
        macd REAL,
        stochastic REAL,
        vwma REAL,
        timeframe TEXT
    )
    """

    with connection:
        with connection.cursor() as cursor:
            cursor.execute(create_table_query)

            insert_query = """
            INSERT INTO predictions (date, code, signal, last_trade_price, sma_20, sma_50, ema_12, ema_26, rsi, macd, stochastic, vwma, timeframe)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """

            for _, row in data.iterrows():
                values = (
                    row.name,
                    row['code'],
                    row['Signal'],
                    row['last_trade_price'] if pd.notna(row['last_trade_price']) else None,
                    row['SMA_20'] if pd.notna(row['SMA_20']) else None,
                    row['SMA_50'] if pd.notna(row['SMA_50']) else None,
                    row['EMA_12'] if pd.notna(row['EMA_12']) else None,
                    row['EMA_26'] if pd.notna(row['EMA_26']) else None,
                    row['RSI'] if pd.notna(row['RSI']) else None,
                    row['MACD'] if pd.notna(row['MACD']) else None,
                    row['Stochastic'] if pd.notna(row['Stochastic']) else None,
                    row['VWMA'] if pd.notna(row['VWMA']) else None,
                    row['timeframe']
                )
                cursor.execute(insert_query, values)

    connection.close()


@app.route('/technical-analysis', methods=['POST','GET'])
def technical_analysis():
    try:
        processed_data = process_stock_data()
        if processed_data.empty:
            return jsonify({"error": "No data processed"}), 404

        preview_data = processed_data.head()
        return jsonify(preview_data.to_dict(orient='records')), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5003, debug=True)

