package mk.ukim.finki.mkstockexchange.stockexchangeapp;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.StockDataRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.StockDataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
//@EnableJpaRepositories("mk.ukim.finki.mkstockexchange.stockexchangeapp.repository")
public class StockExchangeAppApplication {
    StockDataRepository stockDataRepository;
    public static void main(String[] args) {
        SpringApplication.run(StockExchangeAppApplication.class, args);
    }


    public void run(String... args) {
        // Пример за користење на репозиторието
        List<StockData> stockData = stockDataRepository.findById_Code("ALK");
        stockData.forEach(System.out::println);
    }
}
