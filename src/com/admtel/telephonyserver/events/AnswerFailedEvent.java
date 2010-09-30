package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class AnswerFailedEvent extends ChannelEvent {

	String failureCause = "";
	public AnswerFailedEvent(Channel channel, String failureCause) {
		super(channel);
		eventType = EventType.AnswerFailed;
		this.failureCause = failureCause;
	}
}
