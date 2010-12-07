package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class QueueJoinedEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "QueueJoinedEvent ["
				+ (queueName != null ? "queueName=" + queueName + ", " : "")
				+ (isAgent != null ? "isAgent=" + isAgent + ", " : "")
				+ (channel != null ? "channel=" + channel : "") + "]";
	}

	private String queueName;
	private Boolean isAgent;	

	public QueueJoinedEvent(Channel channel, String queueName, Boolean isAgent) {
		super(channel);
		eventType = EventType.QueueJoined;
		this.queueName = queueName;
		this.isAgent = isAgent;
	}

	public String getQueueName() {
		return queueName;
	}

	public Boolean isAgent() {
		return isAgent;
	}

}
