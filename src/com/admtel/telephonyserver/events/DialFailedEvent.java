package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DialFailedEvent extends ChannelEvent {

	public enum Cause{Unknown, InvalidNumber, NoAnswer, Congested, Answer}
	
	Cause cause = Cause.Unknown;
	
	public DialFailedEvent(Channel channel) {
		super(channel);
		eventType = EventType.DialFailed;
	}
	public DialFailedEvent (Channel channel, Cause cause){
		super(channel);
		eventType = EventType.DialFailed;
		this.cause = cause;
	}

	public Cause getCause() {
		return cause;
	}


	public void setCause(Cause cause) {
		this.cause = cause;
	}

}
