package com.admtel.telephonyserver.remote;

public class SimpleMessageDto extends EventDto {
	public SimpleMessageDto(){
		
	}
	public SimpleMessageDto(String message){
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	String message;
}
