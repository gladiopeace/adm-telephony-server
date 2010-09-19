package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

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
	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
