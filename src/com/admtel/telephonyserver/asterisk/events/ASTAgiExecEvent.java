package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTAgiExecEvent extends ASTChannelEvent {

	public ASTAgiExecEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.AgiExec;
	}
	public boolean isStart(){
		String subEvent = values.get("SubEvent");
		if (subEvent != null && subEvent.equals("Start")){
			return true;
		}
		return false;
	}
	public boolean isEnd(){
		String subEvent = values.get("SubEvent");
		if(subEvent != null && subEvent.equals("End")){
			return true;
		}
		return false;
	}
	public boolean isSuccess(){
		String result = values.get("Result");
		if (result != null && result.equals("Success")){
			return true;
		}
		return false;
	}
}
