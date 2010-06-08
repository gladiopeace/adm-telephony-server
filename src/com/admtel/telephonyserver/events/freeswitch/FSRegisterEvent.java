package com.admtel.telephonyserver.events.freeswitch;

import java.util.Map;

public class FSRegisterEvent extends FSEvent {

	Boolean registered = true;
	public FSRegisterEvent(String switchId, Map values, Boolean registered) {
		super(switchId, values);
		eventType = EventType.FsRegister;
		this.registered = registered;
	}
	public Boolean getRegistered() {
		return registered;
	}
	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}
	public String getUser(){
		return values.get("from-user");
	}

}
