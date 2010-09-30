package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlaybackStartedEvent extends ChannelEvent {

	
	public PlaybackStartedEvent(Channel channel) {
		super(channel);
		eventType = EventType.PlaybackStarted;
	}
}
