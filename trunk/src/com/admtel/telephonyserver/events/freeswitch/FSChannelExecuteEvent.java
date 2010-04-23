package com.admtel.telephonyserver.events.freeswitch;

import java.util.Map;

public class FSChannelExecuteEvent extends FSChannelEvent {

	public FSChannelExecuteEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelExecute;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	
	public String toString(){
		return super.toString()+", "+values.get("Application")+","+values.get("Application-Data");
	}

}
