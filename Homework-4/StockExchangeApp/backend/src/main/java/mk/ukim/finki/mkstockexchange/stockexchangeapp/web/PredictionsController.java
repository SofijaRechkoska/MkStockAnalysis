package mk.ukim.finki.mkstockexchange.stockexchangeapp.web;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.PredictionRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.NewsPredictionService;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.PredictionService;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl.NewsPredictionsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/predictions")
public class PredictionsController {

    private final PredictionService predictionService;
    private final NewsPredictionService newsPredictionsService;

    public PredictionsController(PredictionService predictionService, NewsPredictionService newsPredictionsService) {
        this.predictionService = predictionService;
        this.newsPredictionsService = newsPredictionsService;
    }


    @GetMapping("/signal-percentages")
    public Map<String, Object> getSignalPercentages(
            @RequestParam String stockCode,
            @RequestParam String timeFrame) {

        return predictionService.getSignalPercentages(stockCode,timeFrame);
    }

    @GetMapping("/news-prediction")
    public Map<String, Object> getNewsPrediction(@RequestParam String stockCode){

        return newsPredictionsService.getNewsForCodePredictions(stockCode);
    }
}
