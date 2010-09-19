package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class QueueFailedEvent extends ChannelEvent {

	private String queueName;
	private String reason;

	public QueueFailedEvent(Channel channel, String queueName, String reason) {//TODO change reason to enum
		super(channel);
		eventType = EventType.QueueJoinFailed;
		this.queueName = queueName;
		this.reason = reason;
	}

	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
