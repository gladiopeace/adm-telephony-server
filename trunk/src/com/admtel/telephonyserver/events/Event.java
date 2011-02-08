package com.admtel.telephonyserver.events;

public abstract class Event {
	
	
	public enum EventType {		
		Hangup,
		Answered,
		PlaybackStarted,
		PlaybackEnded,
		PlayAndGetDigitsStarted,
		PlayAndGetDigitsEnded,
		Alerting, 		
		DTMF, AnswerFailed, 
		PlayAndGetDigitsFailed, 
		PlaybackFailed, 
		HangupFailed, 		 
		DialFailed, 
		Linked, 
		DialStarted, ConferenceJoined, ConferenceLeft, ConferenceTalk, QueueJoined, QueueLeft, QueueJoinFailed, ConferenceMuted, ChannelListed, QueueBridged, AcdQueueBridged, AcdQueueFailed, AcdQueueJoined, AcdQueueLeft, AcdQueueBridgeFailed,
	}
	
	protected EventType eventType;	
	public EventType getEventType(){
		return eventType;
	}	
}
