package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class QueueJoinedEvent extends ChannelEvent {

	private String queueName;

	public QueueJoinedEvent(Channel channel, String queueName) {
		super(channel);
		eventType = EventType.QueueJoined;
		this.queueName = queueName;
	}

	public String getQueueName() {
		return queueName;
	}

}
