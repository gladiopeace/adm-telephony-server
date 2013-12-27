package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlaybackFailedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlaybackFailedEvent [");
		if (channel != null) {
			builder.append("channel=");
			builder.append(channel);
			builder.append(", ");
		}
		if (eventType != null) {
			builder.append("eventType=");
			builder.append(eventType);
			builder.append(", ");
		}
		if (cause != null) {
			builder.append("cause=");
			builder.append(cause);
		}
		builder.append("]");
		return builder.toString();
	}

	String cause;
	
	public PlaybackFailedEvent(Channel channel, String cause) {
		super(channel);
		eventType = EventType.PlaybackFailed;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
}
