package com.admtel.telephonyserver.core;

import java.util.Map.Entry;

public class ChannelData extends VariableMap {
	
	public final static String CALLER_ID_NAME = "CallerIdName";
	public final static String CALLER_ID_NUMBER="CallerIdNumber";
	public final static String CALLED_NUMBER="CalledNumber";
	
	public void setCallerIdName(String callerIdName){
		addVariable(CALLER_ID_NAME, callerIdName);
	}
	public String getCallerIdName(){
		return getVariable(CALLER_ID_NAME);
	}
	public void setCallerIdNumber(String callerIdNumber){
		addVariable(CALLER_ID_NUMBER, callerIdNumber);
	}
	public String getCallerIdNumber(){
		return getVariable(CALLER_ID_NUMBER);
	}
	public void setCalledNumber(String calledNumber){
		addVariable(CALLED_NUMBER, calledNumber);
	}
	public String getCalledNumber(){
		return getVariable(CALLED_NUMBER);
	}
	public String toString(){
		String result = super.toString();
		
		for (Entry<String, String> entry:entrySet()){
			result +=","+entry.getKey()+":"+entry.getValue();
		}
		return result;
	}
}
