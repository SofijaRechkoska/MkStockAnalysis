package mk.ukim.finki.mkstockexchange.stockexchangeapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Predictions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String date;
    private String signal;
    private Double last_trade_price;
    private Double sma_20;
    private Double sma_50;
    private Double ema_12;
    private Double ema_26;
    private Double rsi;
    private Double macd;
    private Double stochastic;
    private Double vwma;
    private String timeframe;

    public Predictions(){}


    public Predictions(Long id, String code, String date, String signal, Double last_trade_price, Double sma_20, Double sma_50, Double ema_12, Double ema_26, Double rsi, Double macd, Double stochastic, Double vwma, String timeframe) {
        this.id = id;
        this.code = code;
        this.date = date;
        this.signal = signal;
        this.last_trade_price = last_trade_price;
        this.sma_20 = sma_20;
        this.sma_50 = sma_50;
        this.ema_12 = ema_12;
        this.ema_26 = ema_26;
        this.rsi = rsi;
        this.macd = macd;
        this.stochastic = stochastic;
        this.vwma = vwma;
        this.timeframe = timeframe;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public Double getLast_trade_price() {
        return last_trade_price;
    }

    public void setLast_trade_price(Double last_trade_price) {
        this.last_trade_price = last_trade_price;
    }

    public Double getSma_20() {
        return sma_20;
    }

    public void setSma_20(Double sma_20) {
        this.sma_20 = sma_20;
    }

    public Double getSma_50() {
        return sma_50;
    }

    public void setSma_50(Double sma_50) {
        this.sma_50 = sma_50;
    }

    public Double getEma_12() {
        return ema_12;
    }

    public void setEma_12(Double ema_12) {
        this.ema_12 = ema_12;
    }

    public Double getEma_26() {
        return ema_26;
    }

    public void setEma_26(Double ema_26) {
        this.ema_26 = ema_26;
    }

    public Double getRsi() {
        return rsi;
    }

    public void setRsi(Double rsi) {
        this.rsi = rsi;
    }

    public Double getMacd() {
        return macd;
    }

    public void setMacd(Double macd) {
        this.macd = macd;
    }

    public Double getStochastic() {
        return stochastic;
    }

    public void setStochastic(Double stochastic) {
        this.stochastic = stochastic;
    }

    public Double getVwma() {
        return vwma;
    }

    public void setVwma(Double vwma) {
        this.vwma = vwma;
    }
}






