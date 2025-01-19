package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Predictions;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.PredictionRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.PredictionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PredictionServiceImpl implements PredictionService {

    private final PredictionRepository predictionRepository;
    private final RestTemplate restTemplate;

    public PredictionServiceImpl(PredictionRepository predictionRepository, RestTemplate restTemplate) {
        this.predictionRepository = predictionRepository;
        this.restTemplate = restTemplate;
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

    @Scheduled(fixedRate = 60 * 1000)
    @PostConstruct
    public void updateStockData() {
        // Use the Python service name defined in Docker Compose
        String pythonServiceUrl = "http://127.0.0.1:5000/technical-analysis";  // Ensure this is the correct URL

        try {
            restTemplate.getForEntity(pythonServiceUrl, String.class);
            System.out.println("Python service triggered successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error triggering Python service: " + e.getMessage());
        }
    }




}
