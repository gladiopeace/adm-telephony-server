package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class PlaybackEndedEvent extends ChannelEvent {

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
