package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.UserRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException();
        }

        return userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(RuntimeException::new);
    }
}
