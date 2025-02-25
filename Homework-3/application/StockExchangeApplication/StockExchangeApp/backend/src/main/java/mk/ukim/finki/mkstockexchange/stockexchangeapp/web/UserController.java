package mk.ukim.finki.mkstockexchange.stockexchangeapp.web;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.repository.UserRepository;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    

}
