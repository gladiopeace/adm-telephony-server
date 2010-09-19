package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class QueueJoinedEvent extends ChannelEvent {

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

	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
