package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DialStartedEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "["
				+ (channel != null ? "channel=" + channel + ", " : "")
				+ (eventType != null ? "eventType=" + eventType + ", " : "")
				+ (dialedChannel != null ? "dialedChannel=" + dialedChannel
						: "") + "]";
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
