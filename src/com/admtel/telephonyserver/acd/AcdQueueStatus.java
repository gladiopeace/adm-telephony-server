package com.admtel.telephonyserver.acd;

public enum AcdQueueStatus {
	Disabled(0),
	Enabled(1);
	
	private final int code;
	AcdQueueStatus(int code){
		this.code = code;
	}
	public int toInteger(){
		return this.code;
	}
}
