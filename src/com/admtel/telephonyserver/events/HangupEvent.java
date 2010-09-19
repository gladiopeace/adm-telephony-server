package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class HangupEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "HangupEvent [hangCause=" + hangCause + ", hangupCauseStr="
				+ hangupCauseStr + "]";
	}

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

	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
