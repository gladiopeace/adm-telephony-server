package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class InboundAlertingEvent extends ChannelEvent {

	String callerIdNumber;
	String callerIdName;
	String calledIdNumber;
	
	public InboundAlertingEvent(Channel channel) {
		super(channel);
		eventType=EventType.InboundAlerting;
	}

	public String getCallerIdNumber() {
		return callerIdNumber;
	}

	public void setCallerIdNumber(String callerIdNumber) {
		this.callerIdNumber = callerIdNumber;
	}

	public String getCallerIdName() {
		return callerIdName;
	}

	public void setCallerIdName(String callerIdName) {
		this.callerIdName = callerIdName;
	}

	public String getCalledIdNumber() {
		return calledIdNumber;
	}

	public void setCalledIdNumber(String calledIdNumber) {
		this.calledIdNumber = calledIdNumber;
	}

	public InboundAlertingEvent(Channel channel, String callerIdNumber,
			String callerIdName, String calledIdNumber) {
		super(channel);
		this.callerIdNumber = callerIdNumber;
		this.callerIdName = callerIdName;
		this.calledIdNumber = calledIdNumber;
	}
	
}
