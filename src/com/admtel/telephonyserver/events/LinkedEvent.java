package com.admtel.telephonyserver.events;


import com.admtel.telephonyserver.core.Channel;

public class LinkedEvent extends ChannelEvent {
	@Override
	public String toString() {
		return "LinkedEvent [" + (peer != null ? "peer=" + peer + ", " : "")
				+ (channel != null ? "channel=" + channel : "") + "]";
	}
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
