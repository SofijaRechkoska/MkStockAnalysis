package mk.ukim.finki.mkstockexchange.stockexchangeapp.service;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Favorites;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FavoritesService {
    List<Favorites> getFavoritesForUser(String username);
    void addFavorites(String username, String stockCode);
    void saveFavorites(Favorites favorites);

}
