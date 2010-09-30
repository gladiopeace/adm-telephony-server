package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DialStartedEvent extends ChannelEvent {

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
