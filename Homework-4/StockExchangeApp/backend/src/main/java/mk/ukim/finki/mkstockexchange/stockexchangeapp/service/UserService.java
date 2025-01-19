package mk.ukim.finki.mkstockexchange.stockexchangeapp.service;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;

import java.util.Optional;

public interface UserService {
    User register(String username, String email, String password, String repeatPassword,String name,String surname);

    User loadUserByUsernameAndPassword(String username,String password);

    Optional<User> findByUsername(String username);

}
