package com.admtel.telephonyserver.events;

public class ConferenceUnlockedEvent extends Event {
	public String conferenceId;
	public ConferenceUnlockedEvent(String conferenceId){
		eventType = EventType.ConferenceUnlocked;
		this.conferenceId = conferenceId;
	}
}
