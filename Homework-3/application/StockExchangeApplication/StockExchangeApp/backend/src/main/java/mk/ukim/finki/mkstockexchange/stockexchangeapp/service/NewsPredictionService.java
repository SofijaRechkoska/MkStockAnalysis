package mk.ukim.finki.mkstockexchange.stockexchangeapp.service;

import java.util.Map;

public interface NewsPredictionService {
    public Map<String, Object> getNewsForCodePredictions(String code);
}
