package com.example.VotingApplication.Entity;

import jakarta.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // ✅ Ensures proper foreign key relationship
    @JoinColumn(name = "voter_id", nullable = false)
    private User voter;

    @ManyToOne  // ✅ Links Vote to Candidate
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }
}
