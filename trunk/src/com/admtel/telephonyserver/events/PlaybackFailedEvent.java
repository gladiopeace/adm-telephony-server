package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlaybackFailedEvent extends ChannelEvent {

	String cause;
	
	public PlaybackFailedEvent(Channel channel, String cause) {
		super(channel);
		eventType = EventType.PlaybackFailed;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

}
