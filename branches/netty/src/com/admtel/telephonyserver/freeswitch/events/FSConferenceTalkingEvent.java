package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.freeswitch.events.FSEvent.EventType;

public class FSConferenceTalkingEvent extends FSChannelEvent {

	public FSConferenceTalkingEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ConferenceTalking;
	}

	public String getConferenceName() {
		return values.get("Conference-Name");
	}

	public String getMemberId() {
		return values.get("Member-ID");
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	
	public boolean isOn(){
		String actionStr = values.get("Action");
		if (actionStr != null && actionStr.equals("start-talking")){
			return true;
		}
		return false;
	}

}
