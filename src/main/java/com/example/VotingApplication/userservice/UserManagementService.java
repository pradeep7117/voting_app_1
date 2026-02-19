package com.example.VotingApplication.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VotingApplication.Entity.Status;
import com.example.VotingApplication.Entity.User;
import com.example.VotingApplication.userRepo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Fetch all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Update user status
    public String updateUserStatus(Long userId, Status newStatus) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(newStatus);
            userRepository.save(user);
            return "User status updated successfully.";
        } else {
            return "User not found.";
        }
    }
}
