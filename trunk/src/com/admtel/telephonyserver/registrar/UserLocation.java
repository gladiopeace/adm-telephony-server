package com.admtel.telephonyserver.registrar;

public class UserLocation {
	String user;
	String protocol;
	String switchId;
	
	public UserLocation(String switchId, String protocol, String user) {
		super();
		this.user = user;
		this.protocol = protocol;
		this.switchId = switchId;
	}
	@Override
	public String toString() {
		return "UserLocation [protocol=" + protocol + ", switchId=" + switchId
				+ ", user=" + user + "]";
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getSwitchId() {
		return switchId;
	}
	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}
	
}
