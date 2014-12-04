package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTDtmfEvent extends ASTChannelEvent {

	public ASTDtmfEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.Dtmf;
	}
	public String getDigit(){
		return values.get("Digit");
	}
	public boolean isBegin(){
		if (values.containsKey("Begin")){
			return values.get("Begin").equals("Yes");
		}
		return false;
	}
	public boolean isEnd(){
		if (values.containsKey("End")){
			return values.get("End").equals("Yes");
		}
		return false;
	}

}
