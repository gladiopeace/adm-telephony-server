package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class LinkedEvent extends ChannelEvent {
	Channel peer = null;
	public LinkedEvent(Channel channel, Channel peer) {
		super(channel);
		eventType = EventType.Linked;
		this.peer = peer;
	}
	public Channel getPeerChannel(){
		return peer;
	}
	
}
