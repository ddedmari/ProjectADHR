package com.broids.projectadhr.models;

import com.broids.projectadhr.utils.ServiceType;

public class MyEnrollmentItem {

	private String name;
	private String attributeOne;
	private String attributeTwo;
	private String attributeThree;
	private String attributeFour;
	private ServiceType serviceType;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAttributeOne() {
		return attributeOne;
	}
	public void setAttributeOne(String attributeOne) {
		this.attributeOne = attributeOne;
	}
	public String getAttributeTwo() {
		return attributeTwo;
	}
	public void setAttributeTwo(String attributeTwo) {
		this.attributeTwo = attributeTwo;
	}
	public String getAttributeThree() {
		return attributeThree;
	}
	public void setAttributeThree(String attributeThree) {
		this.attributeThree = attributeThree;
	}
	public String getAttributeFour() {
		return attributeFour;
	}
	public void setAttributeFour(String attributeFour) {
		this.attributeFour = attributeFour;
	}
	public ServiceType getServiceType() {
		return serviceType;
	}
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	
	
	
}
