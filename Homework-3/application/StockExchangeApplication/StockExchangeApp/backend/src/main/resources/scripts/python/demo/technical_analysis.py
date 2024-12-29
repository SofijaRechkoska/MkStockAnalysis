import pandas as pd
import psycopg2
import os
from dotenv import load_dotenv
from psycopg2 import sql
from ta.trend import SMAIndicator, EMAIndicator, MACD
from ta.momentum import RSIIndicator, StochasticOscillator
from ta.volume import VolumeWeightedAveragePrice

load_dotenv()

def get_data_from_db():
    connection = psycopg2.connect(
        host=os.getenv('DB_HOST'),
        port=os.getenv('DB_PORT'),
        database=os.getenv('DB_NAME'),
        user=os.getenv('DB_USER'),
        password=os.getenv('DB_PASSWORD')
    )

    query = "SELECT * FROM public.stock_data"


    try:
        data = pd.read_sql(query, connection)
    except Exception as e:
        print("Error reading data:", e)
        data = pd.DataFrame()

    connection.close()
    return data


def process_stock_data():
    data = get_data_from_db()

    if data.empty:
        print("No data found in the database.")
        return

    data.set_index('date', inplace=True)

    data['last_trade_price'] = pd.to_numeric(data['last_trade_price'].str.replace(',', ''), errors='coerce')
    data['max_price'] = pd.to_numeric(data['max_price'].str.replace(',', ''), errors='coerce')
    data['min_price'] = pd.to_numeric(data['min_price'].str.replace(',', ''), errors='coerce')
    data['avg_price'] = pd.to_numeric(data['avg_price'].str.replace(',', ''), errors='coerce')
    data['volume'] = pd.to_numeric(data['volume'], errors='coerce')  # Ensure volume is numeric

    data.dropna(subset=['last_trade_price', 'max_price', 'min_price', 'volume'], inplace=True)

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

    data = calculate_indicators(data)
    data = generate_signals(data)

    save_predictions_to_db(data)

    return data


def save_predictions_to_db(data):
    connection = psycopg2.connect(
        host=os.getenv('DB_HOST'),
        port=os.getenv('DB_PORT'),
        database=os.getenv('DB_NAME'),
        user=os.getenv('DB_USER'),
        password=os.getenv('DB_PASSWORD')
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
        vwma REAL
    )
    """

    with connection:
        with connection.cursor() as cursor:
            cursor.execute(create_table_query)

            insert_query = """
            INSERT INTO predictions (date, code, signal, last_trade_price, sma_20, sma_50, ema_12, ema_26, rsi, macd, stochastic, vwma)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            for _, row in data.iterrows():
                cursor.execute(insert_query, (
                    row.name,  # Date as index
                    row['code'],
                    row['Signal'],
                    row['last_trade_price'],
                    row['SMA_20'],
                    row['SMA_50'],
                    row['EMA_12'],
                    row['EMA_26'],
                    row['RSI'],
                    row['MACD'],
                    row['Stochastic'],
                    row['VWMA']
                ))

    connection.close()


processed_data = process_stock_data()
print(processed_data.head())
