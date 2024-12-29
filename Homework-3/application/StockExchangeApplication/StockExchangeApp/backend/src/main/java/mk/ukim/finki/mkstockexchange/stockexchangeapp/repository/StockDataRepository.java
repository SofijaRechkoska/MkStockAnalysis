package mk.ukim.finki.mkstockexchange.stockexchangeapp.repository;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockData;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.StockDataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockDataRepository extends JpaRepository<StockData, StockDataId> {

    List<StockData> findById_Code(String code);
    @Query("SELECT DISTINCT sd.id.code FROM StockData sd")
    List<String> findDistinctCodes();
}