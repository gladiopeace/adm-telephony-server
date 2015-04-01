package com.admtel.telephonyserver.registrar;

import com.admtel.telephonyserver.core.SigProtocol;
import com.admtel.telephonyserver.core.Switch;

public class UserLocation {
	String username;
	SigProtocol protocol;
	String switchId;
	String address;
	
	public UserLocation(String switchId, SigProtocol protocol, String username, String address) {
		super();
		this.username = username;
		this.protocol = protocol;
		this.switchId = switchId;
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "UserLocation [username=" + username + ", protocol=" + protocol
				+ ", switchId=" + switchId + ", address=" + address + "]";
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String user) {
		this.username = user;
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
