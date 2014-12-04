package com.admtel.telephonyserver.events;

public class ConferenceLockedEvent extends Event {
	public String conferenceId;
	public ConferenceLockedEvent(String conferenceId){
		eventType = EventType.ConferenceLocked;
		this.conferenceId = conferenceId;
	}
}
