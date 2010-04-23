package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;


public class DtmfEvent extends ChannelEvent {
	
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
