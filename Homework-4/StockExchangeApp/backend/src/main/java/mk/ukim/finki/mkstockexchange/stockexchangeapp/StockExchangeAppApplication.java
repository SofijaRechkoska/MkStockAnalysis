package mk.ukim.finki.mkstockexchange.stockexchangeapp;

import io.github.cdimascio.dotenv.Dotenv;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.StockDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class StockExchangeAppApplication {

    public static void main(String[] args) {
        String dotenvPath = System.getenv("DOTENV_PATH");
        if (dotenvPath == null) {
            dotenvPath = "StockExchangeApp/backend";
        }

        Dotenv dotenv = Dotenv.configure().directory(dotenvPath).load();

        System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        SpringApplication.run(StockExchangeAppApplication.class, args);
    }

    @EventListener
    public void handleShutdownEvent(ContextClosedEvent event) {
        System.out.println("Application is shutting down.");
    }

    @PostConstruct
    public void init() {
        System.out.println("StockExchangeApp initialized successfully!");
    }
}