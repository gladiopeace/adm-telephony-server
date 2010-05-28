package com.admtel.telephonyserver.radius;

public class AuthorizeResult {
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
