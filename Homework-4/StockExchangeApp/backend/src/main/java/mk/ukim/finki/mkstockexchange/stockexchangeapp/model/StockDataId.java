package mk.ukim.finki.mkstockexchange.stockexchangeapp.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StockDataId implements Serializable {
    private String code;
    private String date;

    public StockDataId() {}

    public StockDataId(String code, String date) {
        this.code = code;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockDataId that = (StockDataId) o;
        return Objects.equals(code, that.code) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, date);
    }

    @Override
    public String toString() {
        return "StockDataId{" +
                "code='" + code + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
