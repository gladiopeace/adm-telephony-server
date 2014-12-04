package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTNewStateEvent extends ASTChannelEvent {

	public ASTNewStateEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.NewState;
	}
	public ASTChannelState getChannelState(){
		return ASTChannelState.fromString(values.get("ChannelState"));
	}
	
	public String getCallerIdNum(){
		return values.get("CallerIDNum");
	}
}
