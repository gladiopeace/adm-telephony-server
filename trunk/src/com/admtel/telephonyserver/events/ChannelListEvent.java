package com.admtel.telephonyserver.events;

import java.util.ArrayList;
import java.util.List;

import com.admtel.telephonyserver.core.Channel;

public class ChannelListEvent extends Event {
	ArrayList<Channel> channels = new ArrayList<Channel>();
	public ChannelListEvent(List<Channel> channels){
		eventType = EventType.ChannelListed;
		this.channels.addAll(channels);
	}
}
