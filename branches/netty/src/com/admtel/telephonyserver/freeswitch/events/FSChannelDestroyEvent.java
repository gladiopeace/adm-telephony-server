package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSChannelDestroyEvent extends FSChannelEvent {

	public FSChannelDestroyEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelDestroy;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}

}
