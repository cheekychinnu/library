package com.foo.library.model;

public class ReturnResponse {
	private Boolean isDueDateMissed = false;
	private Penalty penalty;

	public ReturnResponse() {
		super();
	}

	public ReturnResponse(Penalty penalty) {
		this.penalty = penalty;
		this.isDueDateMissed = true;
	}

	public Boolean getIsDueDateMissed() {
		return isDueDateMissed;
	}

	public void setIsDueDateMissed(Boolean isDueDateMissed) {
		this.isDueDateMissed = isDueDateMissed;
	}

	public Penalty getPenalty() {
		return penalty;
	}

	public void setPenalty(Penalty penalty) {
		this.penalty = penalty;
	}

}
