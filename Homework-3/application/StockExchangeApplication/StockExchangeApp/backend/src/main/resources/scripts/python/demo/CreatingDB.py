import psycopg2
from psycopg2 import sql
import os
from dotenv import load_dotenv

load_dotenv()

def connect_to_db():
    return psycopg2.connect(
        host=os.getenv('DB_HOST'),
        port=os.getenv('DB_PORT'),
        database=os.getenv('DB_NAME'),
        user=os.getenv('DB_USER'),
        password=os.getenv('DB_PASSWORD')
    )

def create_table():
    conn = connect_to_db()
    cursor = conn.cursor()

    cursor.execute("DROP TABLE IF EXISTS stock_data;")
    cursor.execute("DROP TABLE IF EXISTS codes;")

    create_codes_table = """
       CREATE TABLE IF NOT EXISTS codes (
           code VARCHAR(255) PRIMARY KEY
       );
       """

    create_stock_data_table = """
        CREATE TABLE IF NOT EXISTS stock_data (
            code VARCHAR(255),
            date DATE,
            last_trade_price VARCHAR(255),
            max_price VARCHAR(255),
            min_price VARCHAR(255),
            avg_price VARCHAR(255),
            percentage_change VARCHAR(255),
            volume VARCHAR(255),
            turnover_best VARCHAR(255),
            total_turnover VARCHAR(255),
            PRIMARY KEY (code, date),
            FOREIGN KEY (code) REFERENCES codes(code) ON DELETE CASCADE
        );
        """

    cursor.execute(create_codes_table)
    cursor.execute(create_stock_data_table)
    conn.commit()
    cursor.close()
    conn.close()

create_table()
