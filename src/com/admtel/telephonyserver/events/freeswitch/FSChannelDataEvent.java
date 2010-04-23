package com.admtel.telephonyserver.events.freeswitch;

import java.util.Map;

public class FSChannelDataEvent extends FSChannelEvent {

	public FSChannelDataEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelData;
	}
	public String getChannelId() {
		return values.get("Channel-Unique-ID");
	}
}
