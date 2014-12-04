package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlaybackEndedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlaybackEndedEvent [");
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
		if (interruptingDigit != null) {
			builder.append("interruptingDigit=");
			builder.append(interruptingDigit);
			builder.append(", ");
		}
		builder.append("success=");
		builder.append(success);
		builder.append(", ");
		if (interruptedFile != null) {
			builder.append("interruptedFile=");
			builder.append(interruptedFile);
		}
		builder.append("]");
		return builder.toString();
	}

	String interruptingDigit="";
	boolean success;
	String interruptedFile="";	

	public PlaybackEndedEvent(Channel channel, String interruptingDigit, String interruptedFile) {
		super(channel);
		eventType = EventType.PlaybackEnded;
		this.interruptingDigit = interruptingDigit;
		this.interruptedFile = interruptedFile;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getInterruptingDigit() {
		return interruptingDigit;
	}

	public void setInterruptingDigit(String interruptingDigit) {
		this.interruptingDigit = interruptingDigit;
	}
	public boolean isInterrupted(){
		return !interruptingDigit.isEmpty();
	}

	public String getInterruptedFile() {
		return interruptedFile;
	}

	public void setInterruptedFile(String interruptedFile) {
		this.interruptedFile = interruptedFile;
	}
}
