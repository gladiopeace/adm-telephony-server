package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.SigProtocol;

public class RegisteredEvent extends Event {
	private String user;
	private SigProtocol sigProtocol;
	private String switchId;

	public RegisteredEvent(String switchId, SigProtocol sigProtocol, String user){
		eventType = EventType.Registered;
		this.user = user;
		this.sigProtocol = sigProtocol;
		this.switchId = switchId;
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
}
