package com.admtel.telephonyserver.events.freeswitch;

import java.util.Map;

public class FSHeartBeatEvent extends FSEvent {

	public FSHeartBeatEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.HeartBeat;
	}

}
