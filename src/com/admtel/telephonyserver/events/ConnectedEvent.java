package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;

public class ConnectedEvent extends ChannelEvent {

	public ConnectedEvent(Channel channel) {
		super(channel);
		eventType = EventType.Connected;
	}
}
