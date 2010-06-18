package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

import com.admtel.telephonyserver.events.DialFailedEvent;
import com.admtel.telephonyserver.events.DialFailedEvent.Cause;

public class ASTOriginateResponseEvent extends ASTChannelEvent {
	
	String channel="";
	
	public ASTOriginateResponseEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.OriginateResponse;
		String actionId = values.get("ActionID");
		if (actionId != null && actionId.contains("___")){
			String[]vals = actionId.split("___");
			if (vals.length==2){
				channel=vals[0];
			}
		}		
	}
	public boolean isSuccess(){
		String response = values.get("Response");
		if (response == null || response.equals("Failure")){
			return false;
		}
		return true;
	}
	@Override
	public String getChannelId() {
		return channel;
	}
	
	public String getDialedDestination(){
		return values.get("Channel");
	}

	public DialFailedEvent.Cause getReason(){
		//return values.get("Reason");
		String reason = values.get("Reason");
		Cause result = Cause.Unknown;
		try{
			int iCause = Integer.valueOf(reason);
			switch (iCause){
			case 0:
					result = Cause.InvalidNumber;
				break;
			case 1:
				result =Cause.NoAnswer;
				break;
			case 4:
				result = Cause.Answer;
				break;
			case 8:
					result = Cause.Congested;
				break;
			}
		}
		catch (Exception e){
			
		}
		return DialFailedEvent.Cause.Unknown;
	}

}
