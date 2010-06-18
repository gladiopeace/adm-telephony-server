package com.admtel.telephonyserver.radius;

import java.util.ArrayList;
import java.util.List;

public class AuthorizeResult {
	
	String userName;
	List<String> routes = new ArrayList<String>();
	
	public List<String> getRoutes() {
		return routes;
	}
	public void setRoutes(List<String> routes) {
		this.routes = routes;
	}
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
