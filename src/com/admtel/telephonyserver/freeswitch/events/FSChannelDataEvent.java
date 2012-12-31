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
	}	public String getCallerIdNum(){
		return values.get("Caller-Caller-ID-Number");
	}
	public String getCalledIdNum(){
		return values.get("Caller-Destination-Number");
	}
	public String getUserName(){
		return values.get("Caller-Username");
	}
	public String getChannelAddress(){
		return values.get("Caller-Network-Addr");
	}

}
