package mk.ukim.finki.mkstockexchange.stockexchangeapp.web;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.StockDataRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {
    @Autowired
    private final StockDataRepository stockDataRepository;

    public StockController(StockDataRepository stockDataRepository) {
        this.stockDataRepository = stockDataRepository;
    }

    @GetMapping("/api/stock-data")
    public Map<String, Object> getStockData() {
        // Sample data for demonstration
        List<Map<String, Object>> stocks = List.of(
                Map.of("symbol", "ALK", "price", 3245.03, "changePercent", 0.25, "date", "2024-12-03"),
                Map.of("symbol", "ADIN", "price", 1584.23, "changePercent", 0.15, "date", "2024-12-03"),
                Map.of("symbol", "KMB", "price", 8455.61, "changePercent", 0.08, "date", "2024-12-03"),
                Map.of("symbol", "MTUR", "price", 6574.11, "changePercent", 0.18, "date", "2024-12-03")
        );

        List<Map<String, Object>> favorites = List.of(
                Map.of("symbol", "ALK", "price", 3245.03, "averagePrice", 15358.157),
                Map.of("symbol", "GRNT", "price", 3245.03, "averagePrice", 11487.998),
                Map.of("symbol", "KMB", "price", 3245.03, "averagePrice", 8455.612)
        );
        List<String> codes = stockDataRepository.findDistinctCodes();
        List<String> sortedCodes = codes.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        // Return a structured JSON response
        return Map.of("stocks", stocks, "favorites", favorites, "codes", sortedCodes);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/favorites")
    public Map<String, Object> getFavorites() {
        List<Map<String, Object>> favorites = List.of(
                Map.of("symbol", "ALK", "price", 3245.03, "averagePrice", 15358.157, "maxPrice", 25.269, "minPrice", 5.421, "changePercent", 0.25),
                Map.of("symbol", "GRNT", "price", 3245.03, "averagePrice", 11487.998, "maxPrice", 1.679, "minPrice", 878.01, "changePercent", -0.15),
                Map.of("symbol", "KMB", "price", 3245.03, "averagePrice", 8455.612, "maxPrice", 24.969, "minPrice", 2.763, "changePercent", 0.08)
        );

        return Map.of("favorites", favorites);
    }

    @GetMapping("/api/markets")
    public String getMarkets() {
        List<StockData> findMarkets = stockDataRepository.findAll();

        StringBuilder response = new StringBuilder();
        for (StockData stockData : findMarkets) {
            response.append(stockData.toString()).append("\n");  // You can customize the format
        }

        return response.toString();
    }




}