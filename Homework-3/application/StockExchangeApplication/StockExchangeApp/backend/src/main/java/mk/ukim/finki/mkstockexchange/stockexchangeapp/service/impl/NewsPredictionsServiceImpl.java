package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.NewsPredictions;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.NewsPredictionsRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.NewsPredictionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        long neutralCount = newsPredictions.stream()
                .filter(prediction -> "NEUTRAL".equalsIgnoreCase(prediction.getSentiment()))
                .count();

        long positiveCount = newsPredictions.stream()
                .filter(prediction -> "POSITIVE".equalsIgnoreCase(prediction.getSentiment()))
                .count();

        long negativeCount = newsPredictions.stream()
                .filter(prediction -> "NEGATIVE".equalsIgnoreCase(prediction.getSentiment()))
                .count();

        String recommendation;
        if (positiveCount > negativeCount && positiveCount > neutralCount) {
            recommendation = "The stock will rise";
        } else if (negativeCount > positiveCount && negativeCount > neutralCount) {
            recommendation = "The stock will fall";
        } else {
            recommendation = "The stock will remain stable";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("company_name", code);
        result.put("recommendation", recommendation);

        return result;
    }


}
