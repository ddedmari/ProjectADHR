package com.broids.projectadhr.models;

public class HealthUser {

	private long _id;
	private String uid;
	private String dotsAadhaar;
	private String isActive;
	private String enrollmentDate;
	private String lastDoseDate;
	private String nextDoseDate;
	private String treatmentType;
	private byte[] picture;
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getDotsAadhaar() {
		return dotsAadhaar;
	}
	public void setDotsAadhaar(String dotsAadhaar) {
		this.dotsAadhaar = dotsAadhaar;
	}
	public boolean isActive() {
		return isActive.equalsIgnoreCase("true");
	}
	public void setActive(String isActive) {
		this.isActive = isActive;
	}
	public String getEnrollmentDate() {
		return enrollmentDate;
	}
	public void setEnrollmentDate(String enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
	public String getLastDoseDate() {
		return lastDoseDate;
	}
	public void setLastDoseDate(String lastDoseDate) {
		this.lastDoseDate = lastDoseDate;
	}
	public String getNextDoseDate() {
		return nextDoseDate;
	}
	public void setNextDoseDate(String nextDoseDate) {
		this.nextDoseDate = nextDoseDate;
	}
	public String getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}
	public byte[] getPhotoURI() {
		return picture;
	}
	public void setPhotoURI(byte[] photoURI) {
		this.picture = photoURI;
	}
	
}
