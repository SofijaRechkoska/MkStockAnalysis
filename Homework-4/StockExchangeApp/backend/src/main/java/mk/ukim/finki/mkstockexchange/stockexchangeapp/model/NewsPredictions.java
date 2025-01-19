package mk.ukim.finki.mkstockexchange.stockexchangeapp.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import lombok.Data;

@Entity
@Data
public class NewsPredictions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company")
    private String companyName;
    private String content;
    private String sentiment;
    private String recommendation;

    public NewsPredictions() {
    }
    public NewsPredictions(String company_name, String content, String sentiment, String recommendation) {
        this.companyName = company_name;
        this.content = content;
        this.sentiment = sentiment;
        this.recommendation = recommendation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}


