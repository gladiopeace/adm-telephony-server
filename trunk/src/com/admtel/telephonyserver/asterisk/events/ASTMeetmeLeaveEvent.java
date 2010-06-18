package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTMeetmeLeaveEvent extends ASTChannelEvent {

	public ASTMeetmeLeaveEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.MeetmeLeave;
	}
	public String  getMeetme(){
		return values.get("Meetme");
	}
	public String getUsernum(){
		return values.get("Usernum");
	}
}
