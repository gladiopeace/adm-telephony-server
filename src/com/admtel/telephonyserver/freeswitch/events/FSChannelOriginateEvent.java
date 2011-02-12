package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSChannelOriginateEvent extends FSChannelEvent {

	public FSChannelOriginateEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelOriginate;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}	
}
