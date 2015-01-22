package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSConferenceJoinedEvent extends FSChannelEvent {

	public FSConferenceJoinedEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ConferenceJoined;
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

}
