package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class AcdQueueJoinedEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "AcdQueueJoinedEvent ["
				+ (queueName != null ? "queueName=" + queueName + ", " : "")
				+ (isAgent != null ? "isAgent=" + isAgent : "") + "]";
	}

	private String queueName;
	private Boolean isAgent;	

	public AcdQueueJoinedEvent(Channel channel, String queueName, Boolean isAgent) {
		super(channel);
		eventType = EventType.AcdQueueJoined;
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
