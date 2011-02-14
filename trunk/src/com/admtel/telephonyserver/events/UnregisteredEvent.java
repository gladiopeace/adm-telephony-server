package com.admtel.telephonyserver.events;

public class UnregisteredEvent extends Event {
	private String user;

	public UnregisteredEvent(String user){
		eventType = EventType.Unregistered;
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
