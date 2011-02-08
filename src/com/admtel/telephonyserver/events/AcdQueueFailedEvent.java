package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class AcdQueueFailedEvent extends ChannelEvent {

	private String queueName;
	private String reason;

	@Override
	public String toString() {
		return "AcdQueueFailedEvent ["
				+ (queueName != null ? "queueName=" + queueName + ", " : "")
				+ (reason != null ? "reason=" + reason : "") + "]";
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
