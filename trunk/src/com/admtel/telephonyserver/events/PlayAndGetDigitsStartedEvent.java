package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlayAndGetDigitsStartedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlayAndGetDigitsStartedEvent [");
		if (channel != null) {
			builder.append("channel=");
			builder.append(channel);
			builder.append(", ");
		}
		if (eventType != null) {
			builder.append("eventType=");
			builder.append(eventType);
		}
		builder.append("]");
		return builder.toString();
	}

	public PlayAndGetDigitsStartedEvent(Channel channel) {
		super(channel);
		eventType = EventType.PlayAndGetDigitsStarted;
	}
}
