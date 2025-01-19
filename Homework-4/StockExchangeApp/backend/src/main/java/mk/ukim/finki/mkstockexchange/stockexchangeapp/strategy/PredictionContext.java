package mk.ukim.finki.mkstockexchange.stockexchangeapp.strategy;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PredictionContext {

    private final Map<String, PredictionStrategy> strategies;

    public PredictionContext(Map<String, PredictionStrategy> strategies) {
        this.strategies = strategies;
    }

    public Map<String, Object> executeStrategy(String strategyKey, String stockCode, String timeFrame) {
        PredictionStrategy strategy = strategies.get(strategyKey);
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy not found: " + strategyKey);
        }
        return strategy.getPrediction(stockCode, timeFrame);
    }
}