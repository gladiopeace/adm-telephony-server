package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.events.Event;

public class FSSessionDisconnectEvent extends FSEvent {

	public FSSessionDisconnectEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.SessionDisconnect;
	}
}
