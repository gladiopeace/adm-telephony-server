package com.admtel.telephonyserver.requests;

abstract public class Request {
	public enum RequestType {HangupRequest};
	
	RequestType type;
	public Request(RequestType type){
		this.type = type;
	}
	public RequestType getType() {		
		return type;
	}
}
