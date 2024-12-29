package mk.ukim.finki.mkstockexchange.stockexchangeapp;

import io.github.cdimascio.dotenv.Dotenv;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.StockDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class StockExchangeAppApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();

        System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        SpringApplication.run(StockExchangeAppApplication.class, args);
    }

}
