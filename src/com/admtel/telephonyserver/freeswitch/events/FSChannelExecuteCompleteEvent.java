package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSChannelExecuteCompleteEvent extends FSChannelEvent {

	public FSChannelExecuteCompleteEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelExecuteComplete;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	
	public String toString(){
		return super.toString()+", "+values.get("Application")+","+values.get("Application-Data");
	}


}
