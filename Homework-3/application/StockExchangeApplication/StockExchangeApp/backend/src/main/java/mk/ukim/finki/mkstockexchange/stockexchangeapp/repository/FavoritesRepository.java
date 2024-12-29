package mk.ukim.finki.mkstockexchange.stockexchangeapp.repository;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.Favorites;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FavoritesRepository extends JpaRepository<Favorites,Long> {
    List<Favorites> findByUser1(User user);
}