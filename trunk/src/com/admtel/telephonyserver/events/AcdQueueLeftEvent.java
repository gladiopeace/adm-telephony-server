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
		return "AcdQueueLeftEvent ["
				+ (queueName != null ? "queueName=" + queueName + ", " : "")
				+ (reason != null ? "reason=" + reason + ", " : "")
				+ (isAgent != null ? "isAgent=" + isAgent : "") + "]";
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
