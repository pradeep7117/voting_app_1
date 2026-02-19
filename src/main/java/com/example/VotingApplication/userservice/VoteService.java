package com.example.VotingApplication.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VotingApplication.Entity.Candidate;
import com.example.VotingApplication.Entity.User;
import com.example.VotingApplication.Entity.Vote;
import com.example.VotingApplication.userRepo.CandidateRepository;
import com.example.VotingApplication.userRepo.UserRepository;
import com.example.VotingApplication.userRepo.VoteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    public boolean castVote(Long voterId, String candidateName) {
        Optional<User> voterOpt = userRepository.findById(voterId);
        Optional<Candidate> candidateOpt = candidateRepository.findByName(candidateName);

        if (voterOpt.isEmpty() || candidateOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid voter or candidate.");
        }

        User voter = voterOpt.get();
        Candidate candidate = candidateOpt.get();

        if (voteRepository.existsByVoter(voter)) {
            return false; // Already voted
        }

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        voteRepository.save(vote);

        return true;
    }

    public List<Object[]> getVoteResults() {
        List<Object[]> voteResults = voteRepository.countVotesByCandidate();

        if (voteResults.isEmpty()) {
            return candidateRepository.findAll().stream()
                    .map(candidate -> new Object[]{candidate.getName(), 0})
                    .toList();
        }

        return voteResults;
    }

    public Vote getVoteByVoter(Long voterId) {
        Optional<User> voterOpt = userRepository.findById(voterId);
        return voterOpt.flatMap(voteRepository::findByVoter).orElse(null);
    }
}
