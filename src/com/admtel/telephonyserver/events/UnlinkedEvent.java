package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class UnlinkedEvent extends ChannelEvent {

	Channel peer = null;
	public UnlinkedEvent(Channel channel, Channel peer) {
		super(channel);
		eventType = EventType.Unlinked;
	}
	public Channel getPeerChannel(){
		return peer;
	}	
}
