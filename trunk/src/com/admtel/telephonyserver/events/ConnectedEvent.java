package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;

public class ConnectedEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "[" + (channel != null ? "channel=" + channel + ", " : "")
				+ (eventType != null ? "eventType=" + eventType : "") + "]";
	}

	public ConnectedEvent(Channel channel) {
		super(channel);
		eventType = EventType.Connected;
	}
}
