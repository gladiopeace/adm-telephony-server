package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.freeswitch.events.FSEvent.EventType;

public class FSConferenceDeafEvent extends FSChannelEvent {

	public FSConferenceDeafEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ConferenceDeaf;
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
	
	public boolean isDeafened(){
		String deafStr = values.get("Action");
		if (deafStr != null && deafStr.equals("deaf-member")){
			return true;
		}
		return false;
	}
}
