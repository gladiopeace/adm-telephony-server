package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class QueueLeftEvent extends ChannelEvent {

	private String queueName;
	private String reason;

	public QueueLeftEvent(Channel channel, String queueName, String reason) { //TODO change reason to enum
		super(channel);
		eventType = EventType.QueueLeft;
		this.queueName = queueName;
		this.reason = reason;
	}

}
