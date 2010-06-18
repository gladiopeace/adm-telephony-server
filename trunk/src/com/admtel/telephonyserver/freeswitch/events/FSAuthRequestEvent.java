package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.events.Event;

public class FSAuthRequestEvent extends FSEvent {

	public FSAuthRequestEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.AuthRequest;
	}

}
