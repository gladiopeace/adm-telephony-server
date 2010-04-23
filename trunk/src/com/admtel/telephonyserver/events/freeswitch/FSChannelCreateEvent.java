package com.admtel.telephonyserver.events.freeswitch;

import java.util.Map;

public class FSChannelCreateEvent extends FSChannelEvent {

	public enum CallDirection {Inbound, Outbound};
	
	public FSChannelCreateEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelCreate;
	}

	@Override
	public String getChannelId() {		
		return values.get("Unique-ID");
	}
	
	public CallDirection getDirection(){
		String direction = values.get("Call-Direction");
		if (direction.equals("inbound")){
			return CallDirection.Inbound;
		}
		return CallDirection.Outbound;
	}

}
