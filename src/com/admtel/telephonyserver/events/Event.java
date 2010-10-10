package com.admtel.telephonyserver.events;

public abstract class Event {
	
	
	public enum EventType {		
		Hangup,
		Answered,
		PlaybackStarted,
		PlaybackEnded,
		PlayAndGetDigitsStarted,
		PlayAndGetDigitsEnded,
		InboundAlerting, 		
		DTMF, AnswerFailed, 
		PlayAndGetDigitsFailed, 
		PlaybackFailed, 
		HangupFailed, 
		OutboundAlerting, 
		DialFailed, 
		Linked, 
		DialStarted, ConferenceJoined, ConferenceLeft, ConferencedTalk, QueueJoined, QueueLeft, QueueJoinFailed, ConferenceMuted, ChannelListed,
	}
	
	protected EventType eventType;	
	public EventType getEventType(){
		return eventType;
	}	
}
