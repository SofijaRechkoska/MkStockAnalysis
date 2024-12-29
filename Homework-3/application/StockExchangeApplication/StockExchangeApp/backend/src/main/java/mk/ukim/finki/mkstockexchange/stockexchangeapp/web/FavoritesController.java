package mk.ukim.finki.mkstockexchange.stockexchangeapp.web;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Favorites;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.FavoritesRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.FavoritesService;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/favorites")
public class FavoritesController {

    private FavoritesRepository favoritesRepository;
    private HttpSession session;


    public FavoritesController(FavoritesRepository favoritesRepository, FavoritesService favoritesService, UserService userService, HttpSession session) {
        this.favoritesRepository = favoritesRepository;
        this.session = session;
    }


    @PostMapping("/add")
    public ResponseEntity<String> addFavorite(@RequestBody Favorites favorite) {
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
        Favorites favoriteToRemove = favoritesRepository.findById(favoriteId).orElse(null);

        if (favoriteToRemove == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite not available");
        }

        favoritesRepository.delete(favoriteToRemove);

        return ResponseEntity.ok("Favorite removed successfully!");
    }


    @GetMapping("/get")
    public Map<String,Object> getFavorites() {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return Map.of("error", "User not logged in");
        }

        List<Favorites> favorites = favoritesRepository.findByUser1(user);
        List<Map<String, Object>> favoritesData = favorites.stream()
                .map(favorite -> {
                    Map<String, Object> favoriteMap = new HashMap<>();
                    favoriteMap.put("id",favorite.getId());
                    favoriteMap.put("code", favorite.getCode());
                    favoriteMap.put("averagePrice", favorite.getAveragePrice());
                    favoriteMap.put("maxPrice", favorite.getMaxPrice());
                    favoriteMap.put("minPrice", favorite.getMinPrice());
                    favoriteMap.put("changePercent", favorite.getPercentageChange());
                    return favoriteMap;
                })
                .toList();
        return Map.of("favorites",favoritesData);
    }
}