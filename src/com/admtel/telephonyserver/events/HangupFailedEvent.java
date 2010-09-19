package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class HangupFailedEvent extends ChannelEvent {

	String failureCause;
	public HangupFailedEvent(Channel channel, String failureCause) {
		super(channel);
		eventType = EventType.HangupFailed;
		this.failureCause = failureCause;
	}
	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
