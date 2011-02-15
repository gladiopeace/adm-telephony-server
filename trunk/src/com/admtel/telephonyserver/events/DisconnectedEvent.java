package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DisconnectedEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "DisconnectedEvent ["
				+ (disconnectCauseStr != null ? "disconnectCauseStr="
						+ disconnectCauseStr + ", " : "") + "disconnectCause="
				+ disconnectCause + "]";
	}

	String disconnectCauseStr;
	int disconnectCause;	
	
	public DisconnectedEvent(Channel channel) {
		super(channel);
		eventType = EventType.Disconnected;		
	}
	
	public String getDisconnectCauseStr() {
		return disconnectCauseStr;
	}

	public void setDisconnectCauseStr(String disconnectCauseStr) {
		this.disconnectCauseStr = disconnectCauseStr;
	}

	public int getDisconnectCause() {
		return disconnectCause;
	}

	public void setDisconnectCause(int disconnectCause) {
		this.disconnectCause = disconnectCause;
	}
}
