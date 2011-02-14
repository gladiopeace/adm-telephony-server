package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;

public class OfferedEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "OfferedEvent [" + (channel != null ? "channel=" + channel : "")
				+ "]";
	}

	public OfferedEvent(Channel channel) {
		super(channel);
		eventType=EventType.Offered;
	}
}
