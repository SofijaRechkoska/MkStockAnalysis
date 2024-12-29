package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;//package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Favorites;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.FavoritesRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.UserRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.FavoritesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final UserRepository userRepository;

    public FavoritesServiceImpl(FavoritesRepository favoritesRepository, UserRepository userRepository) {
        this.favoritesRepository = favoritesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Favorites> getFavoritesForUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return favoritesRepository.findByUser1(user.get());
        }
        throw new IllegalArgumentException("User not found with username: " + username);
    }

    @Override
    public void addFavorites(String username, String stockCode) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Favorites favorite = new Favorites();
            favorite.setUser1(user.get());
            favorite.setCode(stockCode);

            favoritesRepository.save(favorite);
        } else {
            throw new IllegalArgumentException("User not found with username: " + username);
        }
    }

    @Override
    public void saveFavorites(Favorites favorites) {
        if (favorites.getUser1() == null || favorites.getUser1().getUsername() == null) {
            throw new IllegalArgumentException("User or username is missing in favorite.");
        }
        favoritesRepository.save(favorites);
    }
}