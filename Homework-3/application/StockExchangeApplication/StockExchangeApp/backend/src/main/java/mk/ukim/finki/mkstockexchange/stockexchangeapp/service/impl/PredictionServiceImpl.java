package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Predictions;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.PredictionRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.PredictionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PredictionServiceImpl implements PredictionService {

    private final PredictionRepository predictionRepository;

    public PredictionServiceImpl(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    @Override
    public Predictions getPrediction(String stockCode, String timeFrame) {
        return null;
    }

    @Override
    public Map<String, Object> getSignalPercentages(String stockCode, String timeFrame) {
        List<Predictions> predictions = predictionRepository.findByCodeAndTimeframe(stockCode, timeFrame);

        if (predictions.isEmpty()) {
            return null;
        }

        Map<String, Long> signalCounts = predictions.stream()
                .collect(Collectors.groupingBy(Predictions::getSignal, Collectors.counting()));

        long totalPredictions = predictions.size();

        Map<String, Object> result = new HashMap<>();
        result.put("buy", (signalCounts.getOrDefault("Buy", 0L) * 100.0) / totalPredictions);
        result.put("sell", (signalCounts.getOrDefault("Sell", 0L) * 100.0) / totalPredictions);
        result.put("hold", (signalCounts.getOrDefault("Hold", 0L) * 100.0) / totalPredictions);

        return result;
    }
}