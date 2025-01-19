package mk.ukim.finki.mkstockexchange.stockexchangeapp.service;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Predictions;

import java.util.Map;

public interface PredictionService {
    Predictions getPrediction(String stockCode, String timeFrame);
    Map<String, Object> getSignalPercentages(String stockCode, String timeFrame);
}
