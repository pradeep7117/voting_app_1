package com.example.VotingApplication.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.VotingApplication.Entity.Status;
import com.example.VotingApplication.Entity.User;
import com.example.VotingApplication.userRepo.UserRepository;
import com.example.VotingApplication.userservice.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // ✅ Hash password
            user.setStatus(Status.PENDING); // ✅ Default status: PENDING
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Error during registration: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * User Login (For Non-Admins)
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // ✅ Check if password matches
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    if (user.getStatus() == Status.AUTHORIZED) {
                        return ResponseEntity.ok(user); // ✅ Return user data if authorized
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authorized yet.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Login error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Authorize a user
     */
    @PutMapping("/authorize/{id}")
    public ResponseEntity<?> authorizeUser(@PathVariable Long id) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
            user.setStatus(Status.AUTHORIZED);
            User updatedUser = userService.saveUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error during authorization: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all pending users
     */
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingUsers() {
        try {
            List<User> pendingUsers = userService.getAllPendingUsers();
            if (pendingUsers.isEmpty()) {
                return new ResponseEntity<>("No pending users found", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pendingUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching pending users: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
