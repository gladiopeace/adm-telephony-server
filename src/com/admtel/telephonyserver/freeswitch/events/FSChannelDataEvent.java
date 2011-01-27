package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.freeswitch.events.FSChannelStateEvent.CallState;

public class FSChannelDataEvent extends FSChannelEvent {

	CallState callState;
	public FSChannelDataEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelData;
		callState = CallState.valueOf((String)values.get("Channel-Call-State"));
	}
	public String getChannelId() {
		return values.get("Channel-Unique-ID");
	}
	public CallState getCallState(){
		return callState;
	}
}
