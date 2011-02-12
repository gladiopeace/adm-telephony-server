package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

import com.admtel.telephonyserver.events.DialStatus;

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

	public DialStatus getReason(){
		//return values.get("Reason");
		String reason = values.get("Reason");
		DialStatus result = DialStatus.Unknown;
		try{
			int iCause = Integer.valueOf(reason);
			switch (iCause){
			case 0:
					result = DialStatus.InvalidNumber;
				break;
			case 1:
				result = DialStatus.NoAnswer;
				break;
			case 4:
				result = DialStatus.Answer;
				break;
			case 8:
					result = DialStatus.Congested;
				break;
			}
		}
		catch (Exception e){
			
		}
		return DialStatus.Unknown;
	}

}
