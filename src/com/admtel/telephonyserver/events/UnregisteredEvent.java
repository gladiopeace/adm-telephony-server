package com.admtel.telephonyserver.events;

public class UnregisteredEvent extends Event {
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UnregisteredEvent [");
		if (eventType != null) {
			builder.append("eventType=");
			builder.append(eventType);
			builder.append(", ");
		}
		if (user != null) {
			builder.append("user=");
			builder.append(user);
		}
		builder.append("]");
		return builder.toString();
	}

	private String user;
	private String registrationId;

	public UnregisteredEvent(String registrationId, String user){
		eventType = EventType.Unregistered;
		this.user = user;
		this.registrationId = registrationId;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
