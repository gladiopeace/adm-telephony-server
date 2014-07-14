package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTShutdownEvent extends ASTEvent{

	public ASTShutdownEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		this.eventType = EventType.Shutdown;
	}

}
