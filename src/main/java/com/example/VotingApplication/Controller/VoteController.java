package com.example.VotingApplication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.VotingApplication.Entity.Vote;
import com.example.VotingApplication.userservice.VoteService;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @PostMapping("/cast")
    public ResponseEntity<String> castVote(@RequestParam Long voterId, @RequestParam String candidateName) {
        try {
            boolean success = voteService.castVote(voterId, candidateName);
            if (success) {
                return ResponseEntity.ok("Vote cast successfully!");
            } else {
                return ResponseEntity.badRequest().body("You have already voted!");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error casting vote: " + e.getMessage());
        }
    }

    @GetMapping("/results")
    public ResponseEntity<List<Object[]>> getResults() {
        return ResponseEntity.ok(voteService.getVoteResults());
    }

    @GetMapping("/status")
    public ResponseEntity<?> getVoteStatus(@RequestParam Long voterId) {
        Vote vote = voteService.getVoteByVoter(voterId);
        if (vote != null) {
            return ResponseEntity.ok(new VoteStatusResponse(vote.getCandidate().getName()));
        } else {
            return ResponseEntity.ok(new VoteStatusResponse("No vote recorded."));
        }
    }

    static class VoteStatusResponse {
        public String candidateName;
        public VoteStatusResponse(String candidateName) {
            this.candidateName = candidateName;
        }
    }
}
