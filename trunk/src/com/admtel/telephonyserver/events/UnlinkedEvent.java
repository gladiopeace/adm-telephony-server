package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class UnlinkedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UnlinkedEvent [");
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
		if (peer != null) {
			builder.append("peer=");
			builder.append(peer);
		}
		builder.append("]");
		return builder.toString();
	}
	Channel peer = null;
	public UnlinkedEvent(Channel channel, Channel peer) {
		super(channel);
		eventType = EventType.Unlinked;
	}
	public Channel getPeerChannel(){
		return peer;
	}	
}
