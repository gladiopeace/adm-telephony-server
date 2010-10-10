package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class HangupEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "HangupEvent [hangCause=" + hangupCause + ", hangupCauseStr="
				+ hangupCauseStr + "]";
	}

	String hangupCauseStr;
	int hangupCause;
	
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

	public int getHangupCause() {
		return hangupCause;
	}

	public void setHangupCause(int hangupCause) {
		this.hangupCause = hangupCause;
	}
}
