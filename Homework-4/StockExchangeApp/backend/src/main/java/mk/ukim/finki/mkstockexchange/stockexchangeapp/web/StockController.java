package mk.ukim.finki.mkstockexchange.stockexchangeapp.web;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Favorites;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockDataId;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.FavoritesRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.StockDataRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {
    @Autowired
    private final StockDataRepository stockDataRepository;
    private final FavoritesRepository favoritesRepository;


    public StockController(StockDataRepository stockDataRepository,FavoritesRepository favoritesRepository) {
        this.stockDataRepository = stockDataRepository;
        this.favoritesRepository = favoritesRepository;
    }

    @GetMapping("/api/stock-codes")
    public Map<String, Object> getStockCodes() {
        List<String> codesList = stockDataRepository.findDistinctCodes();
        List<String> codes=codesList.stream().sorted(Comparator.naturalOrder()).toList();
        return Map.of("code", codes);
    }

    @GetMapping("/api/stock-data")
    public Map<String, Object> getStockData(HttpSession session) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        List<StockData> findMarkets = stockDataRepository.findAll().stream()
                .filter(c -> c.getId().getDate().equals(today))
                .sorted(Comparator.comparing(StockData::getMaxPrice).reversed())
                .distinct()
                .limit(3)
                .collect(Collectors.toList());

        List<Map<String, String>> stocks = findMarkets.stream()
                .map(stockData -> Map.of(
                        "code", stockData.getId().getCode(),
                        "maxPrice", stockData.getMaxPrice(),
                        "changePercent", stockData.getPercentageChange()
                )).distinct().limit(3).toList();

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return Map.of("error", "User not logged in");
        }

        List<Favorites> userFavorites = favoritesRepository.findByUser1(user);

        List<Map<String, String>> favorites = userFavorites.stream()
                .map(favorite -> Map.of(
                        "code", favorite.getCode(),
                        "averagePrice", favorite.getAveragePrice()
                ))
                .collect(Collectors.toList());
        List<String> codes = stockDataRepository.findDistinctCodes();
        List<String> sortedCodes = codes.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        return Map.of("stocks", stocks, "favorites", favorites, "codes", sortedCodes);
    }


    @GetMapping("/api/markets")
    public List<Map<String, String>> getMarkets() {
        List<StockData> findMarkets = stockDataRepository.findAll();
        return findMarkets.stream().map(stockData -> Map.of(
                "code", stockData.getId().getCode(),
                "date", stockData.getId().getDate(),
                "maxPrice", stockData.getMaxPrice(),
                "minPrice", stockData.getMinPrice(),
                "averagePrice", stockData.getAveragePrice(),
                "percentageChange", stockData.getPercentageChange()
        )).collect(Collectors.toList());
    }

    @GetMapping("/api/stock-data/{code}")
    public List<Map<String, Object>> getStockDataForCode(@PathVariable String code) {
        List<StockData> stockDataList = stockDataRepository.findById_Code(code);


        return stockDataList.stream()
                .map(stockData -> {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("date", stockData.getId().getDate());
                    dataMap.put("maxPrice", stockData.getMaxPrice());
                    return dataMap;
                })
                .collect(Collectors.toList());
    }
}