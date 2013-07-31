package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.core.SigProtocol;

public class FSRegisterExpireEvent extends FSEvent {

	SigProtocol protocol;
	
	public FSRegisterExpireEvent(String switchId, SigProtocol protocol, Map values) {
		super(switchId, values);
		eventType = EventType.FsRegisterExpire;
		this.protocol = protocol;
	}
	public SigProtocol getProtocol() {
		return protocol;
	}
	public void setProtocol(SigProtocol protocol) {
		this.protocol = protocol;
	}
	public String getUser(){
		return values.get("user");
	}
}
