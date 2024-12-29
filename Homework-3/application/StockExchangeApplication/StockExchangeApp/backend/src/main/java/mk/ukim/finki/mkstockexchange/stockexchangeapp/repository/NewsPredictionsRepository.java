package mk.ukim.finki.mkstockexchange.stockexchangeapp.repository;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.NewsPredictions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsPredictionsRepository extends JpaRepository<NewsPredictions, Long> {
    List<NewsPredictions> findByCompanyName(String companyName);
}
