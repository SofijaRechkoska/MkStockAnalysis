package mk.ukim.finki.mkstockexchange.stockexchangeapp.strategy.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.NewsPredictions;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.NewsPredictionsRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.strategy.PredictionStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("newsPredictionStrategy")
public class NewsPredictionStrategy implements PredictionStrategy {

    private final NewsPredictionsRepository newsPredictionsRepository;

    public NewsPredictionStrategy(NewsPredictionsRepository newsPredictionsRepository) {
        this.newsPredictionsRepository = newsPredictionsRepository;
    }

    @Override
    public Map<String, Object> getPrediction(String stockCode, String timeFrame) {
        List<NewsPredictions> newsPredictions = newsPredictionsRepository.findByCompanyName(stockCode);

        Map<String, Long> sentimentCounts = newsPredictions.stream()
                .collect(Collectors.groupingBy(NewsPredictions::getSentiment, Collectors.counting()));

        long positiveCount = sentimentCounts.getOrDefault("POSITIVE", 0L);
        long neutralCount = sentimentCounts.getOrDefault("NEUTRAL", 0L);
        long negativeCount = sentimentCounts.getOrDefault("NEGATIVE", 0L);

        String recommendation = getRecommendation(positiveCount, negativeCount, neutralCount);

        Map<String, Object> result = new HashMap<>();
        result.put("company_name", stockCode);
        result.put("recommendation", recommendation);

        return result;
    }

    private String getRecommendation(long positiveCount, long negativeCount, long neutralCount) {
        if (positiveCount > Math.max(negativeCount, neutralCount)) {
            return "The stock will rise";
        } else if (negativeCount > Math.max(positiveCount, neutralCount)) {
            return "The stock will fall";
        } else {
            return "The stock will remain stable";
        }
    }
}