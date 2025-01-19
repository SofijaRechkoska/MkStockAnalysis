package mk.ukim.finki.mkstockexchange.stockexchangeapp.strategy;

import java.util.Map;

public interface PredictionStrategy {
    Map<String, Object> getPrediction(String stockCode, String timeFrame);
}