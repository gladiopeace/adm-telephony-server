package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class AnswerFailedEvent extends ChannelEvent {

	String failureCause = "";
	public AnswerFailedEvent(Channel channel, String failureCause) {
		super(channel);
		eventType = EventType.AnswerFailed;
		this.failureCause = failureCause;
	}
	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
