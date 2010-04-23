package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class OutboundAlertingEvent extends ChannelEvent {

	public OutboundAlertingEvent(Channel channel) {
		super(channel);
		eventType = EventType.OutboundAlerting;
	}

}
