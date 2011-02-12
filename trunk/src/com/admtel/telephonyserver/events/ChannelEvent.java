package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public abstract class ChannelEvent extends Event {
	@Override
	public String toString() {
		return "ChannelEvent [" + (channel != null ? "channel=" + channel : "")
				+ "]";
	}

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
