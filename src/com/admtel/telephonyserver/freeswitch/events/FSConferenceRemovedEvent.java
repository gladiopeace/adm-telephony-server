package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSConferenceRemovedEvent extends FSChannelEvent {

	public FSConferenceRemovedEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ConferenceRemoved;
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
