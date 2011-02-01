package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;

public class AlertingEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "AlertingEvent []";
	}

	public AlertingEvent(Channel channel) {
		super(channel);
		eventType=EventType.Alerting;
	}
}
