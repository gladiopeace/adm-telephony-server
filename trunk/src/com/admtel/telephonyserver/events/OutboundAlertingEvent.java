package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class OutboundAlertingEvent extends ChannelEvent {

	String calledNumber;
	String callerId;
	public OutboundAlertingEvent(Channel channel, String callerId, String calledNumber) {
		super(channel);
		this.callerId = callerId;
		this.calledNumber = calledNumber;
		eventType = EventType.OutboundAlerting;
	}
	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
