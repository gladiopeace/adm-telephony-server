package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class AcdQueueBridgeFailedEvent extends ChannelEvent {

	public AcdQueueBridgeFailedEvent(Channel channel) {
		super(channel);
		eventType = EventType.AcdQueueBridgeFailed;

	}
	@Override
	public String toString() {
		return "AcdQueueBridgeFailedEvent []";
	}

}
