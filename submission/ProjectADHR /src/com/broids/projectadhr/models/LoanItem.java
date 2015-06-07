package com.broids.projectadhr.models;

public class LoanItem {
	private int loanID;
	private String loanName;
	private float rateOfInterest;
	
	public LoanItem(int loanID, String loanName, float rateOfInterest) {
		super();
		this.loanID = loanID;
		this.loanName = loanName;
		this.rateOfInterest = rateOfInterest;
	}

	public int getLoanID() {
		return loanID;
	}

	public void setLoanID(int loanID) {
		this.loanID = loanID;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public float getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(float rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}
	
	
}
