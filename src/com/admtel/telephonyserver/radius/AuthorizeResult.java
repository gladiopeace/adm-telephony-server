package com.admtel.telephonyserver.radius;

public class AuthorizeResult {
	
	String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	Boolean authorized = false;
	Integer allowedTime = 0;
	
	
	public Boolean getAuthorized() {
		return authorized;
	}
	public void setAuthorized(Boolean authorized) {
		this.authorized = authorized;
	}
	public Integer getAllowedTime() {
		return allowedTime;
	}
	public void setAllowedTime(Integer allowedTime) {
		this.allowedTime = allowedTime;
	}
}
