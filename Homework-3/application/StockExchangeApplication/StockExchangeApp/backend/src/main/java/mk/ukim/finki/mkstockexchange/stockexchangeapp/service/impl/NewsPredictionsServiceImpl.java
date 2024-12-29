package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.NewsPredictions;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.NewsPredictionsRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.NewsPredictionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class NewsPredictionsServiceImpl implements NewsPredictionService {

    private final NewsPredictionsRepository newsPredictionsRepository;

    public NewsPredictionsServiceImpl(NewsPredictionsRepository newsPredictionsRepository) {
        this.newsPredictionsRepository = newsPredictionsRepository;
    }

    @Override
    public Map<String, Object> getNewsForCodePredictions(String code) {
        List<NewsPredictions> newsPredictions = newsPredictionsRepository.findByCompanyName(code);

        Map<String, Long> sentimentCounts = newsPredictions.stream()
                .collect(Collectors.groupingBy(NewsPredictions::getSentiment, Collectors.counting()));

        long positiveCount = sentimentCounts.getOrDefault("POSITIVE", 0L);
        long neutralCount = sentimentCounts.getOrDefault("NEUTRAL", 0L);
        long negativeCount = sentimentCounts.getOrDefault("NEGATIVE", 0L);

        String recommendation = getRecommendation(positiveCount, negativeCount, neutralCount);

        Map<String, Object> result = new HashMap<>();
        result.put("company_name", code);
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