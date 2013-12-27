package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

import com.admtel.telephonyserver.asterisk.events.ASTEvent.EventType;

public class ASTVarSetEvent extends ASTChannelEvent {

	String name;
	String value;
	public ASTVarSetEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.VarSet;
		name = values.get("Variable");
		value = values.get("Value");
	}
	public String getName(){
		return name;
	}
	public String getValue(){
		return value;
	}

}
