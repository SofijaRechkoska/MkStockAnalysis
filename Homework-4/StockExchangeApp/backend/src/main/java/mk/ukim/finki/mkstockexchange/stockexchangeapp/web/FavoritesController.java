package mk.ukim.finki.mkstockexchange.stockexchangeapp.web;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Favorites;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.FavoritesRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.FavoritesService;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:80")
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final FavoritesRepository favoritesRepository;


    public FavoritesController(FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFavorite(@RequestBody Favorites favorite, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        favorite.setUser1(user);
        favoritesRepository.save(favorite);

        return ResponseEntity.ok("Favorite added successfully!");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteFavorite(@RequestBody Map<String, Long> body) {
        Long favoriteId = body.get("id");
        return favoritesRepository.findById(favoriteId)
                .map(favorite -> {
                    favoritesRepository.delete(favorite);
                    return ResponseEntity.ok("Favorite removed successfully!");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite not available."));
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getFavorites(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not logged in."));
        }

        List<Favorites> favorites = favoritesRepository.findByUser1(user);
        List<Map<String, Object>> favoritesData = favorites.stream()
                .map(favorite -> {
                    Map<String, Object> favoriteMap = new HashMap<>();
                    favoriteMap.put("id", favorite.getId());
                    favoriteMap.put("code", favorite.getCode());
                    favoriteMap.put("averagePrice", favorite.getAveragePrice());
                    favoriteMap.put("maxPrice", favorite.getMaxPrice());
                    favoriteMap.put("minPrice", favorite.getMinPrice());
                    favoriteMap.put("changePercent", favorite.getPercentageChange());
                    return favoriteMap;
                })
                .toList();

        return ResponseEntity.ok(Map.of("favorites", favoritesData));
    }
}
