package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlayAndGetDigitsFailedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlayAndGetDigitsFailedEvent [");
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

	public String cause;
	
	public PlayAndGetDigitsFailedEvent(Channel channel, String cause) {
		super(channel);
		eventType = EventType.PlayAndGetDigitsFailed;
		this.cause = cause;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
}
