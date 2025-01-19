package mk.ukim.finki.mkstockexchange.stockexchangeapp.service;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;

import java.util.List;

public interface StockDataService {


    List<String> findDistinctCodes();


    List<StockData> findAll();


    List<StockData> findByCode(String code);


    void save(StockData stockData);

    void delete(StockData stockData);


    List<StockData> findTopByMaxPrice(String date, int limit);
}