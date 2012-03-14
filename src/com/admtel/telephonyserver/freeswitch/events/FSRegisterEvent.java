package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.core.SigProtocol;

public class FSRegisterEvent extends FSEvent {

	Boolean registered = true;
	SigProtocol protocol;
	
	public FSRegisterEvent(String switchId, SigProtocol protocol, Map values, Boolean registered) {
		super(switchId, values);
		eventType = EventType.FsRegister;
		this.registered = registered;
		this.protocol = protocol;
	}
	public SigProtocol getProtocol() {
		return protocol;
	}
	public void setProtocol(SigProtocol protocol) {
		this.protocol = protocol;
	}
	public Boolean getRegistered() {
		return registered;
	}
	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}
	public String getUser(){
		return values.get("username");
	}
	public String getRealm(){
		return values.get("realm");
	}
	public String getRegistrationId() {
		return values.get("from-user")+"@"+values.get("from-host");
	}

}
