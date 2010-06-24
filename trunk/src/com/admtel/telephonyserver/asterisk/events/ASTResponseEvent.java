package com.admtel.telephonyserver.asterisk.events;

import java.util.HashMap;
import java.util.Map;

public class ASTResponseEvent extends ASTChannelEvent {

	boolean isSuccess = false;
	String message = "";

	Map<String, String> data = new HashMap<String,String>();
	
	public Map<String, String> getData() {
		return data;
	}

	@Override
	public String toString() {
		return "ASTResponseEvent [isSuccess=" + isSuccess + ", message="
				+ message + ", request=" + request + ", eventType=" + eventType
				+ ", values=" + values + "]";
	}
	String request="";
	
	public ASTResponseEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.Response;
		if (values.get("Response").equals("Success")){
			isSuccess = true;
		}
		message = values.get("Message");
		String actionId = values.get("ActionID");
		if (actionId != null && actionId.contains("___")){
			String[]vals = actionId.split("___");
			if (vals.length==2){
				values.put("Channel", vals[0]);
				request = vals[1];
			}
		}
		if (request.equals("GetVar")){
			String varName = values.get("Variable");
			String varValue = values.get("Value");
			if (varName != null){
				if (varValue == null || varValue.equals("(null)")){
					varValue = null;
				}
				data.put(varName,varValue);
				
			}
		}
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getMessage() {
		return message;
	}
	public String getRequest(){
		return request;
	}
}
