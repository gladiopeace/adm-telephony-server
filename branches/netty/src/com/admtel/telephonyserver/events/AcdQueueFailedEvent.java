package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class AcdQueueFailedEvent extends ChannelEvent {

	private String queueName;
	private String reason;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AcdQueueFailedEvent [");
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
		}
		builder.append("]");
		return builder.toString();
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public AcdQueueFailedEvent(Channel channel, String queueName, String reason) {//TODO change reason to enum
		super(channel);
		eventType = EventType.AcdQueueFailed;
		this.queueName = queueName;
		this.reason = reason;
	}

}
