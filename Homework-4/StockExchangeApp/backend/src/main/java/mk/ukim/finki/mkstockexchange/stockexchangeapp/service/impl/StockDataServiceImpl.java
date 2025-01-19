package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.StockDataRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.StockDataService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockDataServiceImpl implements StockDataService {

    private final StockDataRepository stockDataRepository;

    public StockDataServiceImpl(StockDataRepository stockDataRepository) {
        this.stockDataRepository = stockDataRepository;
    }

    @Override
    public List<String> findDistinctCodes() {
        return stockDataRepository.findDistinctCodes();
    }

    @Override
    public List<StockData> findAll() {
        return stockDataRepository.findAll();
    }

    @Override
    public List<StockData> findByCode(String code) {
        return stockDataRepository.findById_Code(code);
    }

    @Override
    public void save(StockData stockData) {
        stockDataRepository.save(stockData);
    }

    @Override
    public void delete(StockData stockData) {
        stockDataRepository.delete(stockData);
    }

    @Override
    public List<StockData> findTopByMaxPrice(String date, int limit) {
        return stockDataRepository.findAll().stream()
                .filter(stockData -> stockData.getId().getDate().equals(date))
                .sorted(Comparator.comparing(StockData::getMaxPrice).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}