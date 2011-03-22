package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;

public class ConnectedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConnectedEvent [");
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

	public ConnectedEvent(Channel channel) {
		super(channel);
		eventType = EventType.Connected;
	}
}
