package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class PlayAndGetDigitsFailedEvent extends ChannelEvent {

	public String cause;
	
	public PlayAndGetDigitsFailedEvent(Channel channel, String cause) {
		super(channel);
		eventType = EventType.PlayAndGetDigitsFailed;
		this.cause = cause;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
