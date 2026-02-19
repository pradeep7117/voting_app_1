package com.example.VotingApplication.userservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VotingApplication.Entity.Candidate;
import com.example.VotingApplication.Entity.Vote;
import com.example.VotingApplication.userRepo.CandidateRepository;
import com.example.VotingApplication.userRepo.VoteRepository;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private VoteRepository voteRepository;

    // Get a list of all candidates
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    // Add a new candidate
    public Candidate addCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    
    public void incrementVote(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + candidateId));

        // Create a new vote and associate it with the candidate
        Vote vote = new Vote();
        vote.setCandidate(candidate);

        // Save the vote
        voteRepository.save(vote);

        // Save the candidate (not strictly necessary here due to cascading)
        candidateRepository.save(candidate);
    }

    // Calculate vote results for all candidates
    public List<Object[]> getVoteResults() {
        return candidateRepository.calculateVoteResults();
    }
}
