package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public abstract class ChannelEvent extends Event {
	Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public ChannelEvent(Channel channel) {
		super();
		this.channel = channel;
	}
}
