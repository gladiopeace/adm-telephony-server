package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public abstract class FSChannelEvent extends FSEvent {

	public FSChannelEvent(String switchId, Map values) {
		super(switchId, values);
		// TODO Auto-generated constructor stub
	}
	public abstract String getChannelId();
	
	public String getAnswerState(){
		return values.get("Answer-State"); //TODO convert to proper answer state
	}
	public boolean isOutbound(){
		String callDirection = values.get("Call-Direction");
		if (callDirection == null){
			return false;
		}
		return callDirection.equals("outbound");		
	}
	public FSChannelState getChannelState(){
		String answerState = values.get("Answer-State");
		return FSChannelState.fromString(answerState);
	}
	public String getCallerIdNum(){
		return values.get("Channel-Caller-ID-Number");
	}
	public String getCalledIdNum(){
		return values.get("Channel-Destination-Number");
	}

}
