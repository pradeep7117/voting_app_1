package com.example.VotingApplication.userservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.VotingApplication.Entity.Admin;
import com.example.VotingApplication.Entity.Candidate;
import com.example.VotingApplication.Entity.Status;
import com.example.VotingApplication.Entity.User;
import com.example.VotingApplication.Entity.VoteResult;
import com.example.VotingApplication.userRepo.AdminRepository;
import com.example.VotingApplication.userRepo.CandidateRepository;
import com.example.VotingApplication.userRepo.UserRepository;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    public boolean authenticateAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        return admin != null && new BCryptPasswordEncoder().matches(password, admin.getPassword());
    }

    public List<User> getPendingUsers() {
        return userRepository.findByStatus(Status.PENDING);
    }
    public void addCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    public List<Object[]> getVoteResults() {
        // Custom logic to calculate results from votes
        return candidateRepository.calculateVoteResults();
    }
}
