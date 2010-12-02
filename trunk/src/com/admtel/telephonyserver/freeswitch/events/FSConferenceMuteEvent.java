package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.freeswitch.events.FSEvent.EventType;

public class FSConferenceMuteEvent extends FSChannelEvent {

	public FSConferenceMuteEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ConferenceMute;
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
	
	public boolean isMuted(){
		String mutedStr = values.get("Action");
		if (mutedStr != null && mutedStr.equals("mute-member")){
			return true;
		}
		return false;
	}
}
