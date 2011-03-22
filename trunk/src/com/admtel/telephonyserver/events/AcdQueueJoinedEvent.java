package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class AcdQueueJoinedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AcdQueueJoinedEvent [");
		if (channel != null) {
			builder.append("channel=");
			builder.append(channel);
			builder.append(", ");
		}
		if (eventType != null) {
			builder.append("eventType=");
			builder.append(eventType);
			builder.append(", ");
		}
		if (queueName != null) {
			builder.append("queueName=");
			builder.append(queueName);
			builder.append(", ");
		}
		if (isAgent != null) {
			builder.append("isAgent=");
			builder.append(isAgent);
		}
		builder.append("]");
		return builder.toString();
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
