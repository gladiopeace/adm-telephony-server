package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class QueueLeftEvent extends ChannelEvent {

	private String queueName;
	private String reason;
	private Boolean isAgent;

	public QueueLeftEvent(Channel channel, String queueName, Boolean isAgent, String reason) { //TODO change reason to enum
		super(channel);
		eventType = EventType.QueueLeft;
		this.queueName = queueName;
		this.isAgent = isAgent;
		this.reason = reason;
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
