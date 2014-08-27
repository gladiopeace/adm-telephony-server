package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class CoreShowChannelEvent extends ASTChannelEvent {

	public CoreShowChannelEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.CoreShowChannel;
	}

}
