package mk.ukim.finki.mkstockexchange.stockexchangeapp;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.StockDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class StockExchangeAppApplication {
    StockDataRepository stockDataRepository;
    public static void main(String[] args) {
        SpringApplication.run(StockExchangeAppApplication.class, args);
    }


    public void run(String... args) {
        List<StockData> stockData = stockDataRepository.findById_Code("ALK");
        stockData.forEach(System.out::println);
    }
}
