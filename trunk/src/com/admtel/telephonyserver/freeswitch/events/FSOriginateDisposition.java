package com.admtel.telephonyserver.freeswitch.events;

public enum FSOriginateDisposition {
	USER_NOT_REGISTERED, Unknown;
	
	static public FSOriginateDisposition fromString(String str){
		if (str == null) return Unknown;
		try{
			return FSOriginateDisposition.valueOf(str);
		}
		catch (Exception e){
			return Unknown;
		}
	}
}
