package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.StockDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockDataServiceImpl {
    private final StockDataRepository repository;

    @Autowired
    public StockDataServiceImpl(StockDataRepository repository) {
        this.repository = repository;
    }

}