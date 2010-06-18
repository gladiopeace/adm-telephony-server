package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSChannelStateEvent extends FSChannelEvent {

	public FSChannelStateEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelState;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}

}
