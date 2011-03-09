package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSChannelUnbridgeEvent extends FSChannelEvent {

	public FSChannelUnbridgeEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelUnbridge;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	public String getPeerChannel(){
		return values.get("Other-Leg-Unique-ID");
	}

}
