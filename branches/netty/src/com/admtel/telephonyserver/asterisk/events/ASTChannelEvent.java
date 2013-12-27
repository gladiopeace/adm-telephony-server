package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public abstract class ASTChannelEvent extends ASTEvent{

	public ASTChannelEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		// TODO Auto-generated constructor stub
	}
	public String getChannelId(){
		String channelId = values.get("Channel");
		if (channelId == null) return "";
		return channelId;
	}
}
