package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlayAndGetDigitsEndedEvent extends ChannelEvent {

	String digits="";
	boolean success;
	String terminatingDigit="";
	String interruptedFile="";
	
	public PlayAndGetDigitsEndedEvent(Channel channel, String digits) {
		super(channel);
		eventType = EventType.PlayAndGetDigitsEnded;
		this.digits = digits;
	}
	public String getDigits() {
		return digits;
	}

	public void setDigits(String digits) {
		this.digits = digits;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getTerminatingDigit() {
		return terminatingDigit;
	}

	public void setTerminatingDigit(String terminatingDigit) {
		this.terminatingDigit = terminatingDigit;
	}
	public String getInterruptedFile() {
		return interruptedFile;
	}
	public void setInterruptedFile(String interruptedFile) {
		this.interruptedFile = interruptedFile;
	}
	
}
