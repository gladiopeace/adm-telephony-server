package com.admtel.telephonyserver.requests;

abstract public class Request {
	public enum RequestType {HangupRequest, AnswerRequest, ShowChannelsRequest, ReloadRequest, ShowStatusRequest, ShowSwitchRequest, ParticipantMuteRequest, DialRequest};
	
	RequestType type;
	
	public void setType(RequestType type) {
		this.type = type;
	}
	public Request(){
		
	}
	public Request(RequestType type){
		this.type = type;
	}
	public RequestType getType() {		
		return type;
	}
	@Override
	public String toString() {
		return "Request [" + (type != null ? "type=" + type : "") + "]";
	}
}
