package com.admtel.telephonyserver.events.freeswitch;

import java.util.Map;

public class FSChannelOutgoingEvent extends FSChannelEvent {

	public FSChannelOutgoingEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelOutgoing;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	public String getDestinationChannel(){
		return values.get("Other-Leg-Unique-ID");
	}
}
