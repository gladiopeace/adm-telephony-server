package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class ConferenceLeftEvent extends ChannelEvent {

	public ConferenceLeftEvent(Channel channel) {
		super(channel);
		eventType = EventType.ConferenceLeft;
	}

}
