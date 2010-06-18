package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTBridgeEvent extends ASTChannelEvent {

	public ASTBridgeEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.Bridge;
	}

	@Override
	public String getChannelId() {
		String channel = values.get("Channel1");
		return (channel==null?"":channel);
	}
	
	public String getPeerChannel(){
		String channel = values.get("Channel2");
		return (channel==null?"":channel);		
	}

}
