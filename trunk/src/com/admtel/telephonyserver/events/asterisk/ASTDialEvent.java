package com.admtel.telephonyserver.events.asterisk;

import java.util.Map;

public class ASTDialEvent extends ASTChannelEvent {

	public ASTDialEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.Dial;
	}
	public String getDestinationChannel(){
		if (values.containsKey("Destination")){
			return values.get("Destination");
		}
		return "";
	}
	public boolean isBegin(){
		String subEvent = values.get("SubEvent");
		return subEvent != null && subEvent.equals("Begin");
	}
}
