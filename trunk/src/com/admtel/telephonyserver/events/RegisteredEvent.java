package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.SigProtocol;

public class RegisteredEvent extends Event {
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegisteredEvent [");
		if (eventType != null) {
			builder.append("eventType=");
			builder.append(eventType);
			builder.append(", ");
		}
		if (user != null) {
			builder.append("user=");
			builder.append(user);
			builder.append(", ");
		}
		if (sigProtocol != null) {
			builder.append("sigProtocol=");
			builder.append(sigProtocol);
			builder.append(", ");
		}
		if (switchId != null) {
			builder.append("switchId=");
			builder.append(switchId);
		}
		builder.append("]");
		return builder.toString();
	}

	private String user;
	private SigProtocol sigProtocol;
	private String switchId;
	private String address;

	public RegisteredEvent(String switchId, SigProtocol sigProtocol, String user, String address){
		eventType = EventType.Registered;
		this.user = user;
		this.sigProtocol = sigProtocol;
		this.switchId = switchId;
		this.address=address;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public SigProtocol getSigProtocol() {
		return sigProtocol;
	}

	public void setSigProtocol(SigProtocol sigProtocol) {
		this.sigProtocol = sigProtocol;
	}
	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
