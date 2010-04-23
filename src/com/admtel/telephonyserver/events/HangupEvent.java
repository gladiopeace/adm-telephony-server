package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class HangupEvent extends ChannelEvent {

	String hangupCauseStr;
	int hangCause;
	
	public HangupEvent(Channel channel) {
		super(channel);
		eventType = EventType.Hangup;
	}

	public String getHangupCauseStr() {
		return hangupCauseStr;
	}

	public void setHangupCauseStr(String hangupCauseStr) {
		this.hangupCauseStr = hangupCauseStr;
	}

	public int getHangCause() {
		return hangCause;
	}

	public void setHangCause(int hangCause) {
		this.hangCause = hangCause;
	}
	
}
