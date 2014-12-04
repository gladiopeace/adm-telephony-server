package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSChannelBridgeEvent extends FSChannelEvent {

	public FSChannelBridgeEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelBridge;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	public String getPeerChannel(){
		return values.get("Other-Leg-Unique-ID");
	}
}
