package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTNewCalleridEvent extends ASTChannelEvent {

	public ASTNewCalleridEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.NewCallerId;
	}
	public String getCallerIdNum(){
		return values.get("CallerIDNum");
	}
	public String getUserName() {
		String username = values.get("CallerIDName");
		if (username == null){
			username = getCallerIdNum();
		}
		return username;
	}
}
