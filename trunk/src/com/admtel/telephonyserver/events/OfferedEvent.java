package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;

public class OfferedEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "OfferedEvent ["
				+ (super.toString() != null ? "toString()=" + super.toString()
						: "") + "]";
	}

	public OfferedEvent(Channel channel) {
		super(channel);
		eventType=EventType.Offered;
	}
}
