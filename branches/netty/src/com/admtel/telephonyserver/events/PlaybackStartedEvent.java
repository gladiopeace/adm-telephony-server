package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlaybackStartedEvent extends ChannelEvent {

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlaybackStartedEvent [");
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

	public PlaybackStartedEvent(Channel channel) {
		super(channel);
		eventType = EventType.PlaybackStarted;
	}
}
