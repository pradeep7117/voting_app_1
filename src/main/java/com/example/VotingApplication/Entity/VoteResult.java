package com.example.VotingApplication.Entity;


public class VoteResult {
    private String candidateName;
    private int votes;
	public String getCandidateName() {
		return candidateName;
	}
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	public int getVotes() {
		return votes;
	}
	public void setVotes(int votes) {
		this.votes = votes;
	}
	public VoteResult(String candidateName, int votes) {
		super();
		this.candidateName = candidateName;
		this.votes = votes;
	}

    
}
