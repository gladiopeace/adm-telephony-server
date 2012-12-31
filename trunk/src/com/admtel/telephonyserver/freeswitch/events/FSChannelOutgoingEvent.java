package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.utils.CodecsUtils;

public class FSChannelOutgoingEvent extends FSChannelEvent {

	public FSChannelOutgoingEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelOutgoing;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}	
	public String getChannelAddress(){
		return values.get("Caller-Network-Addr");
	}
	public String getCallerDestinationNumber(){
		
		return CodecsUtils.urlDecode(values.get("Caller-Destination-Number"));
	}
	public String getPeerChannel(){
		return values.get("Other-Leg-Unique-ID");
	}

}
