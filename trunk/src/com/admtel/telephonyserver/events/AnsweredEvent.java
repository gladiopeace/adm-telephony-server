package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;

public class AnsweredEvent extends ChannelEvent {

	public AnsweredEvent(Channel channel) {
		super(channel);
		eventType = EventType.Answered;
	}
}
