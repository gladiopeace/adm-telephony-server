package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTMeetmeTalkingEvent extends ASTChannelEvent {

	public ASTMeetmeTalkingEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.MeetmeTalking;
	}
	public String  getMeetme(){
		return values.get("Meetme");
	}
	public String getUsernum(){
		return values.get("Usernum");
	}
	public boolean isOn(){
		return values.get("Status").equals("on");
	}
}
