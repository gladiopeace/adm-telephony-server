package com.admtel.telephonyserver.events.asterisk;

import java.util.Map;

public class ASTNewChannelEvent extends ASTChannelEvent {

	public ASTNewChannelEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.NewChannel;
	}
	public ASTChannelState getChannelState(){
		return ASTChannelState.fromString(values.get("ChannelState"));
	}
	public String getCallerIdNum(){
		return values.get("CallerIDNum");
	}
	public String getCalledNum(){
		return values.get("Exten");
	}
	public String getUserName() {
		String username = values.get("AccountCode");
		if (username == null){
			username = getCallerIdNum();
		}
		return username;
	}
}
