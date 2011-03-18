package com.admtel.telephonyserver.acd;

public enum AcdAgentStatus {

	NotReady ( 0),
	Ready(1),
	Busy(2),
	;
	private final int code;	
	
	AcdAgentStatus(int code){
		this.code = code;
	}
	public int toInteger(){
		return this.code;
	}
}
