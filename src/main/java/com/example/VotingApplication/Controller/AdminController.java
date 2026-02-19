package com.example.VotingApplication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.VotingApplication.Entity.Admin;
import com.example.VotingApplication.Entity.Candidate;
import com.example.VotingApplication.Entity.Role;
import com.example.VotingApplication.Entity.Status;
import com.example.VotingApplication.Entity.User;
import com.example.VotingApplication.Entity.VoteResult;
import com.example.VotingApplication.userRepo.UserRepository;
import com.example.VotingApplication.userservice.AdminService;
import com.example.VotingApplication.userservice.UserManagementService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
	

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserManagementService userManagementService;
		
	@Autowired
	UserRepository userRepository;
	
    @Autowired
    private AdminService adminService;
    

   
    @GetMapping("/pending-users")
    public List<User> getPendingUsers() {
        return adminService.getPendingUsers();
    }

    @PostMapping("/add-candidate")
    public ResponseEntity<String> addCandidate(@RequestBody Candidate candidate) {
        adminService.addCandidate(candidate);
        return ResponseEntity.ok("Candidate added successfully");
    }

    @GetMapping("/results")
    public List<Object[]> getResults() {
        return adminService.getVoteResults();
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // --- UPDATED CHECK: Use passwordEncoder.matches() ---
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) { 
                if (user.getRole() == Role.ADMIN) {
                    return ResponseEntity.ok("Login successful");
                }
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
    
  
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userManagementService.getAllUsers();
    }

  
    @PutMapping("/users/{id}/status")
    public ResponseEntity<String> updateUserStatus(@PathVariable Long id, @RequestParam Status newStatus) {
        String result = userManagementService.updateUserStatus(id, newStatus);
        if (result.equals("User status updated successfully.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<String> createAdmin(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username already exists!");
        }

        user.setRole(Role.ADMIN);
        user.setStatus(Status.AUTHORIZED);

        // --- NEW LINE: ENCRYPT PASSWORD BEFORE SAVING ---
        user.setPassword(passwordEncoder.encode(user.getPassword())); 

        userRepository.save(user);
        return ResponseEntity.ok("Admin user created successfully!");
    }
    
}
