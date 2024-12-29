package mk.ukim.finki.mkstockexchange.stockexchangeapp.repository;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Predictions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Predictions,Long> {
    List<Predictions> findByCodeAndTimeframe(String stockSymbol,String timeframe);
}
