package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class AcdQueueBridgedEvent extends ChannelEvent {

	Channel peerChannel;
	public AcdQueueBridgedEvent(Channel channel, Channel peerChannel) {
		super(channel);
		eventType = EventType.AcdQueueBridged;
		this.peerChannel = peerChannel;
	}
	public Channel getPeerChannel() {
		return peerChannel;
	}
	@Override
	public String toString() {
		return "AcdQueueBridgedEvent ["
				+ (peerChannel != null ? "peerChannel=" + peerChannel : "")
				+ "]";
	}

}
