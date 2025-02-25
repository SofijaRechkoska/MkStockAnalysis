package mk.ukim.finki.mkstockexchange.stockexchangeapp.model;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "stock_data")
@Data
@AllArgsConstructor
public class StockData {

    @EmbeddedId
    private StockDataId id;
    private String lastTradePrice;
    private String maxPrice;
    private String minPrice;

    @Column(name = "avg_price")
    private String averagePrice;

    private String percentageChange;
    private String volume;
    private String turnoverBest;
    private String totalTurnover;

    public StockData() {

    }
    @Override
    public String toString() {
        return "StockData{" +
                "id=" + id.getCode() + " " + id.getDate() +
                ", lastTradePrice=" + lastTradePrice +
                ", maxPrice=" + maxPrice +
                ", minPrice=" + minPrice +
                ", averagePrice=" + averagePrice +
                ", percentageChange=" + percentageChange +
                ", volume=" + volume +
                ", turnoverBest=" + turnoverBest +
                ", totalTurnover=" + totalTurnover +
                '}';
    }


}
