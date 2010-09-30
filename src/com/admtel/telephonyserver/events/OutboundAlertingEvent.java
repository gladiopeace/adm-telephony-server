package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class OutboundAlertingEvent extends ChannelEvent {

	String calledNumber;
	String callerId;
	public OutboundAlertingEvent(Channel channel, String callerId, String calledNumber) {
		super(channel);
		this.callerId = callerId;
		this.calledNumber = calledNumber;
		eventType = EventType.OutboundAlerting;
	}
}
