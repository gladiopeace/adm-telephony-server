package com.admtel.telephonyserver.acd.impl;

public class AcdAgent {
	public enum Status{Ready, Busy};
	String name;
	String address;
	Status status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
}