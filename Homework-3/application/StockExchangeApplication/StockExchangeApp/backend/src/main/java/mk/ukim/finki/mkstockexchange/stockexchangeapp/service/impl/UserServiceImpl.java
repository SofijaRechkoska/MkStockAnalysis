package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.UserRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.UserService;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String username, String email, String password, String repeatPassword,String name,String surname) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new RuntimeException();
        }

        if (!password.equals(repeatPassword)) {
            throw new RuntimeException();
        }

        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException(username);
        }
        User user1 = new User(username,email,password,name,surname);

        return userRepository.save(user1);
    }

    @Override
    public User loadUserByUsernameAndPassword(String username,String password) throws RuntimeException {
        return userRepository.findByUsernameAndPassword(username,password)
                .orElseThrow(() -> new RuntimeException("User does not exist or invalid credentials"));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
