package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class AnsweredEvent extends ChannelEvent {

	public AnsweredEvent(Channel channel) {
		super(channel);
		eventType = EventType.Answered;
	}

	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}
