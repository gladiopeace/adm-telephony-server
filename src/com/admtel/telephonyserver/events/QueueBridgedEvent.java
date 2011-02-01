package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class QueueBridgedEvent extends ChannelEvent {

	Channel peerChannel;
	public QueueBridgedEvent(Channel channel, Channel peerChannel) {
		super(channel);
		eventType = EventType.QueueBridged;
		this.peerChannel = peerChannel;
	}
	public Channel getPeerChannel() {
		return peerChannel;
	}
	@Override
	public String toString() {
		return "QueueBridgedEvent ["
				+ (peerChannel != null ? "peerChannel=" + peerChannel + ", "
						: "")
				+ (channel != null ? "channel=" + channel + ", " : "")
				+ (eventType != null ? "eventType=" + eventType : "") + "]";
	}

}
