package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlayAndGetDigitsStartedEvent extends ChannelEvent {

	public PlayAndGetDigitsStartedEvent(Channel channel) {
		super(channel);
		eventType = EventType.PlayAndGetDigitsStarted;
	}

}
