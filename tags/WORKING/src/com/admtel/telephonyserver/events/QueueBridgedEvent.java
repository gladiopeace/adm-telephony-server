package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class QueueBridgedEvent extends ChannelEvent {

	Channel peerChannel;
	public QueueBridgedEvent(Channel channel, Channel peerChannel) {
		super(channel);
		eventType = EventType.QueueBridged;
		this.peerChannel = peerChannel;
	}
	public Channel getPeerChannel() {
		return peerChannel;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueueBridgedEvent [");
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
		if (peerChannel != null) {
			builder.append("peerChannel=");
			builder.append(peerChannel);
		}
		builder.append("]");
		return builder.toString();
	}

}
