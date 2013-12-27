package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DialStartedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DialStartedEvent [");
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
		if (dialedChannel != null) {
			builder.append("dialedChannel=");
			builder.append(dialedChannel);
		}
		builder.append("]");
		return builder.toString();
	}

	Channel dialedChannel;
	public DialStartedEvent(Channel channel, Channel dialedChannel) {
		super(channel);
		eventType = EventType.DialStarted;
		this.dialedChannel = dialedChannel;
	}

	public Channel getDialedChannel(){
		return dialedChannel;
	}
}
