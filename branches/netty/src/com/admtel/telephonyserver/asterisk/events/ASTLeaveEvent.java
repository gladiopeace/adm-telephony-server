package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTLeaveEvent extends ASTChannelEvent {

	public ASTLeaveEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.Leave;
	}
	public String getQueue(){
		return values.get("Queue");
	}
	public Integer getPosition(){
		try{
			return Integer.valueOf(values.get("Position"));
		}
		catch (Exception e){
			
		}
		return 0;
	}
	public Integer getCount(){
		try{
			return Integer.valueOf(values.get("Count"));
		}
		catch (Exception e){
			
		}
		return 0;		
	}
}
