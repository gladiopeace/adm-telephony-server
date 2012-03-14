package com.admtel.telephonyserver.registrar;

import com.admtel.telephonyserver.core.SigProtocol;
import com.admtel.telephonyserver.core.Switch;

public class UserLocation {
	String registrationId;
	String username;
	SigProtocol protocol;
	String switchId;
	
	public UserLocation(String registrationId, String switchId, SigProtocol protocol, String username) {
		super();
		this.username = username;
		this.protocol = protocol;
		this.switchId = switchId;
		this.registrationId = registrationId;
	}
	@Override
	public String toString() {
		return "UserLocation [protocol=" + protocol + ", switchId=" + switchId
				+ ", user=" + username + "]";
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String user) {
		this.username = user;
	}
	
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public SigProtocol getProtocol() {
		return protocol;
	}
	public void setProtocol(SigProtocol protocol) {
		this.protocol = protocol;
	}
	public String getSwitchId() {
		return switchId;
	}
	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}
	public String getAddress(Switch _switch) {
		if (_switch.getSwitchId().equals(switchId)){
			return String.format("%s:%s", protocol, username);
		}
		else{
			return String.format("%s:%s@%s", protocol, username, _switch.getDefinition().getAddress());
		}
	}
	
}
