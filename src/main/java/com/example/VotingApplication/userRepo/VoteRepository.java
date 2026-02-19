package com.example.VotingApplication.userRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.VotingApplication.Entity.Vote;
import com.example.VotingApplication.Entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByVoter(User voter);

    @Query("SELECT v FROM Vote v WHERE v.voter = :voter")
    Optional<Vote> findByVoter(User voter);

    @Query("SELECT c.name, COALESCE(COUNT(v), 0) FROM Candidate c LEFT JOIN Vote v ON c.id = v.candidate.id GROUP BY c.id")
    List<Object[]> countVotesByCandidate();
}
