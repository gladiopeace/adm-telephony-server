package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTFullyBootedEvent extends ASTEvent {

	public ASTFullyBootedEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		this.eventType = EventType.FullyBooted;
	}

}
