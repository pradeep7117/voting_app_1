package com.example.VotingApplication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.VotingApplication.Entity.LoginRequest;
import com.example.VotingApplication.Entity.LoginResponse;
import com.example.VotingApplication.Entity.User;
import com.example.VotingApplication.userRepo.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

   
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD_HASH = passwordEncoder.encode("admin123");

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        
        if (ADMIN_USERNAME.equals(loginRequest.getUsername()) &&
            passwordEncoder.matches(loginRequest.getPassword(), ADMIN_PASSWORD_HASH)) {
            return ResponseEntity.ok(new LoginResponse("ADMIN"));
        }

        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

           
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.ok(new LoginResponse(user.getRole().toString())); // Returns VOTER or other roles
            } else {
                return ResponseEntity.status(401).body("Invalid password");
            }
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
