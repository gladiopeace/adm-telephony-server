package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DisconnectedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DisconnectedEvent [");
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
		if (disconnectCode != null) {
			builder.append("disconnectCode=");
			builder.append(disconnectCode);
		}
		builder.append("]");
		return builder.toString();
	}

	DisconnectCode disconnectCode;
	public DisconnectedEvent(Channel channel, DisconnectCode disconnectCode) {
		super(channel);
		eventType = EventType.Disconnected;		
		this.disconnectCode = disconnectCode;
	}
	public DisconnectCode getDisconnectCode() {
		return disconnectCode;
	}
}
