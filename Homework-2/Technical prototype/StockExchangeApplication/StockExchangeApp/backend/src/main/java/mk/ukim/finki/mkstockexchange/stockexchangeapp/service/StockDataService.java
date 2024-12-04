package mk.ukim.finki.mkstockexchange.stockexchangeapp.service;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.StockDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockDataService {
    private final StockDataRepository repository;

    @Autowired
    public StockDataService(StockDataRepository repository) {
        this.repository = repository;
    }

    public List<StockData> getAllStocks(){
        return repository.findAll();
    }
    public List<StockData> getStockByCode(String code){
        return repository.findById_Code(code);
    }
}