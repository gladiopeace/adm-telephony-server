package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class ConferenceTalkEvent extends ChannelEvent {

	boolean talking;
	public ConferenceTalkEvent(Channel channel, boolean talking) {
		super(channel);
		eventType = EventType.ConferencedTalk;
		this.talking = talking;
	}
	boolean isTalking(){
		return this.talking;
	}
}
