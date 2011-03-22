package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlayAndGetDigitsEndedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlayAndGetDigitsEndedEvent [");
		if (channel != null) {
			builder.append("channel=");
			builder.append(channel);
			builder.append(", ");
		}
		if (eventType != null) {
			builder.append("eventType=");
			builder.append(eventType);
			builder.append(", ");
		}
		if (digits != null) {
			builder.append("digits=");
			builder.append(digits);
			builder.append(", ");
		}
		builder.append("success=");
		builder.append(success);
		builder.append(", ");
		if (terminatingDigit != null) {
			builder.append("terminatingDigit=");
			builder.append(terminatingDigit);
			builder.append(", ");
		}
		if (interruptedFile != null) {
			builder.append("interruptedFile=");
			builder.append(interruptedFile);
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
}
