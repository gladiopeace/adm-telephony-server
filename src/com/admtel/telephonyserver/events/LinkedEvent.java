package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;

public class LinkedEvent extends ChannelEvent {
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LinkedEvent [");
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
	public LinkedEvent(Channel channel, Channel peer) {
		super(channel);
		eventType = EventType.Linked;
		this.peer = peer;
	}
	public Channel getPeerChannel(){
		return peer;
	}	
}
