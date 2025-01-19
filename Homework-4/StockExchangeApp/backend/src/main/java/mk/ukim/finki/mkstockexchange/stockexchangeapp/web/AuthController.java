package mk.ukim.finki.mkstockexchange.stockexchangeapp.web;


import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.model.User;
import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (user.getPassword() == null || !user.getPassword().equals(user.getRepeatPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        try {
            userService.register(user.getUsername(),user.getEmail(), user.getPassword(),user.getRepeatPassword(), user.getName(), user.getSurname());

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering user: " + e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpSession session) {
        try {
            User existingUser = userService.loadUserByUsernameAndPassword(user.getUsername(), user.getPassword());

            if (existingUser != null && user.getPassword().equals(existingUser.getPassword())) {
                session.setAttribute("user", existingUser);
                session.setAttribute("userId", existingUser.getId());
                session.setAttribute("username", existingUser.getUsername());
                return ResponseEntity.ok(new User(existingUser.getName(), existingUser.getSurname()));
            } else {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error logging in: " + e.getMessage());
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok("User logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during logout: " + e.getMessage());
        }
    }


    @GetMapping("/current-user")
    public Map<String, Object> getCurrentUser(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
                return Map.of("name",user.getName(),"surname",user.getSurname());
            }
        }
        return null;
    }

}
