package com.admtel.telephonyserver.registrar;

import com.admtel.telephonyserver.core.SigProtocol;
import com.admtel.telephonyserver.core.Switch;

public class UserLocation {
	String user;
	SigProtocol protocol;
	String switchId;
	
	public UserLocation(String switchId, SigProtocol protocol, String user) {
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
			return String.format("%s:%s", protocol, user);
		}
		else{
			return String.format("%s:%s@%s", protocol, user, _switch.getDefinition().getAddress());
		}
	}
	
}
