package com.admtel.telephonyserver.events.freeswitch;

import java.util.Map;

public class FSChannelHangupEvent extends FSChannelEvent {

	public FSChannelHangupEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelHangup;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	public String getHangupCause(){
		return values.get("Hangup-Cause");
	}

}
