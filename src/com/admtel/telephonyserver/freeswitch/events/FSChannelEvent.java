package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public abstract class FSChannelEvent extends FSEvent {

	public FSChannelEvent(String switchId, Map values) {
		super(switchId, values);
		// TODO Auto-generated constructor stub
	}
	public abstract String getChannelId();
	
	public boolean isOutbound(){
		String callDirection = values.get("Call-Direction");
		if (callDirection == null){
			return false;
		}
		return callDirection.equals("outbound");		
	}
	public String getCallerIdNum(){
		return values.get("Channel-Caller-ID-Number");
	}
	public String getCalledIdNum(){
		return values.get("Channel-Destination-Number");
	}
	public String getUserName(){
		return values.get("Channel-Username");
	}
	public String getChannelAddress(){
		return values.get("Channel-Network-Addr");
	}
	public String getAccountCode(){
		return values.get("variable_accountcode");
	}

}
