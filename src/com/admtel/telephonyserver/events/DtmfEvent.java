package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DtmfEvent extends ChannelEvent {
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DtmfEvent [");
		if (channel != null) {
			builder.append("channel=");
			builder.append(channel);
			builder.append(", ");
		}
		if (eventType != null) {
			builder.append("eventType=");
			builder.append(eventType);
			builder.append(", ");
		}
		if (digit != null) {
			builder.append("digit=");
			builder.append(digit);
			builder.append(", ");
		}
		if (digitEdge != null) {
			builder.append("digitEdge=");
			builder.append(digitEdge);
		}
		builder.append("]");
		return builder.toString();
	}

	String digit;
	enum DigitEdge{Begin, End};
	
	DigitEdge digitEdge = DigitEdge.End;
	
	public DtmfEvent(Channel channel, String digit){
		super(channel);
		eventType=EventType.DTMF;
	}

	public DtmfEvent(Channel channel, String digit, DigitEdge digitEdge)
	{
		super(channel);
		eventType = EventType.DTMF;
		this.digitEdge = digitEdge;
	}
	public String getDigit() {		
		return digit;
	}

	public void setDigit(String digit) {
		this.digit = digit;
	}
	
	public boolean isEnd(){
		return digitEdge == DigitEdge.End;
	}
}
