package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.core.CallOrigin;

public class FSChannelCreateEvent extends FSChannelEvent {
	
	public FSChannelCreateEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelCreate;
	}

	@Override
	public String getChannelId() {		
		return values.get("Unique-ID");
	}
	public String getPeerChannel(){
		return values.get("Other-Leg-Unique-ID");
	}
}
