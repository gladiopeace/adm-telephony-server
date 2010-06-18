package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

/*Event: MeetmeJoin
Privilege: call,all
Channel: SIP/danny-00000005
Uniqueid: 1261432798.6
Meetme: 1234
Usernum: 1
CallerIDnum: danny
CallerIDname: 122345*/

public class ASTMeetmeJoinEvent extends ASTChannelEvent {

	public ASTMeetmeJoinEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.MeetmeJoin;
	}

	public String  getMeetme(){
		return values.get("Meetme");
	}
	public String getUsernum(){
		return values.get("Usernum");
	}
}
