package com.admtel.telephonyserver.config;

public enum SwitchType {
	Unknown, Asterisk, Freeswitch;
	
	static public SwitchType fromString (String strType){
		if (strType.equals("asterisk")){
			return SwitchType.Asterisk;
		}
		else if (strType.equals("freeswitch")){
			return SwitchType.Freeswitch;
		}
		return SwitchType.Unknown;
	}
}
