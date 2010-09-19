package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class PlaybackStartedEvent extends ChannelEvent {

	
	public PlaybackStartedEvent(Channel channel) {
		super(channel);
		eventType = EventType.PlaybackStarted;
	}

	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}
