package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.remoteapi.Message;

public class PlayAndGetDigitsEndedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlayAndGetDigitsEndedEvent [");
		if (digits != null) {
			builder.append("digits=");
			builder.append(digits);
			builder.append(", ");
		}
		if (interruptedFile != null) {
			builder.append("interruptedFile=");
			builder.append(interruptedFile);
			builder.append(", ");
		}
		builder.append("success=");
		builder.append(success);
		builder.append(", ");
		if (terminatingDigit != null) {
			builder.append("terminatingDigit=");
			builder.append(terminatingDigit);
		}
		builder.append("]");
		return builder.toString();
	}
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
	@Override
	public Message toMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
