package com.admtel.telephonyserver.core;

public enum DisconnectCode {
	Normal(16), NoCircuitAvailable(34);
	
	
	private final int code;
	DisconnectCode(int code){
		this.code = code;
	}
	public Integer toInteger(){
		return code;
	}
}
