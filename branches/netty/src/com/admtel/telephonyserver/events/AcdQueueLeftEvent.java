package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class AcdQueueLeftEvent extends ChannelEvent {

	private String queueName;
	private String reason;
	private Boolean isAgent;

	public AcdQueueLeftEvent(Channel channel, String queueName, Boolean isAgent, String reason) { //TODO change reason to enum
		super(channel);
		eventType = EventType.AcdQueueLeft;
		this.queueName = queueName;
		this.isAgent = isAgent;
		this.reason = reason;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AcdQueueLeftEvent [");
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
		if (reason != null) {
			builder.append("reason=");
			builder.append(reason);
			builder.append(", ");
		}
		if (isAgent != null) {
			builder.append("isAgent=");
			builder.append(isAgent);
		}
		builder.append("]");
		return builder.toString();
	}

	public String getQueueName() {
		return queueName;
	}

	public String getReason() {
		return reason;
	}

	public Boolean isAgent() {
		return isAgent;
	}
}
