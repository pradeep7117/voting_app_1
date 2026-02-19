package com.example.VotingApplication.userservice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VotingApplication.Entity.Status;
import com.example.VotingApplication.Entity.User;
import com.example.VotingApplication.userRepo.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllPendingUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getStatus() == Status.PENDING)
                .toList();
    }
    
    
    
}

