package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTMeetmeMuteEvent extends ASTChannelEvent {

	public ASTMeetmeMuteEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.MeetmeMute;
	}
	public String  getMeetme(){
		return values.get("Meetme");
	}
	public String getUsernum(){
		return values.get("Usernum");
	}
	public boolean isMuted(){
		return values.get("Status").equals("on");
	}

}
