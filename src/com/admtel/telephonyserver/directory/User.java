package com.admtel.telephonyserver.directory;

import java.util.HashMap;
import java.util.Map;

public class User {
	String id;
	String password;
	String domain;
	String account;
	
	Map<String, String> extraInfo = new HashMap<String, String>();
	public User(){
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getExtraInfo(String key) {
		return extraInfo.get(key);
	}
	public void setExtraInfo(String key, String value) {
		this.extraInfo.put(key, value);
	}
	public User(String id, String password, String domain, String account) {
		super();
		this.id = id;
		this.password = password;
		this.domain = domain;
		this.account = account;
	}
}
