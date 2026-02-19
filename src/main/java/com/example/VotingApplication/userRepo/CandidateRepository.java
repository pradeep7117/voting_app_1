package com.example.VotingApplication.userRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.VotingApplication.Entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    @Query("SELECT c.name, COUNT(v.id) AS voteCount FROM Candidate c JOIN Vote v ON c.id = v.candidate.id GROUP BY c.id, c.name")
    List<Object[]> calculateVoteResults();
    Optional<Candidate> findByName(String name);
    @Query("SELECT c FROM Candidate c")
    List<Candidate> getAllCandidates();
}

