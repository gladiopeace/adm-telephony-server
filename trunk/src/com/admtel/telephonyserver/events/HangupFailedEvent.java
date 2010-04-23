package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class HangupFailedEvent extends ChannelEvent {

	String failureCause;
	public HangupFailedEvent(Channel channel, String failureCause) {
		super(channel);
		eventType = EventType.HangupFailed;
		this.failureCause = failureCause;
	}

}
