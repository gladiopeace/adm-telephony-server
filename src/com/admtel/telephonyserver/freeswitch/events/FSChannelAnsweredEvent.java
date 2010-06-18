package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSChannelAnsweredEvent extends FSChannelEvent {

	public FSChannelAnsweredEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelAnswered;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}

}
