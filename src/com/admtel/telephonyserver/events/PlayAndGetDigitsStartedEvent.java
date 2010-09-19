package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class PlayAndGetDigitsStartedEvent extends ChannelEvent {

	public PlayAndGetDigitsStartedEvent(Channel channel) {
		super(channel);
		eventType = EventType.PlayAndGetDigitsStarted;
	}

	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
