package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.events.Event.EventType;

public class RegisterExpireEvent extends Event {
	private String user;

	public RegisterExpireEvent(String user){
		eventType = EventType.RegisterExpire;
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
