package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DestroyEvent extends ChannelEvent {

	public DestroyEvent(Channel channel) {
		super(channel);
		eventType = EventType.Destroy;				
	}

}
