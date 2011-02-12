package com.admtel.telephonyserver.events;

public abstract class Event {
	
	
	public enum EventType {		
		DISCONNECTED,
		CONNECTED,
		PlaybackStarted,
		PlaybackEnded,
		PlayAndGetDigitsStarted,
		PlayAndGetDigitsEnded,
		Alerting, 		
		DTMF, AnswerFailed, 
		PlayAndGetDigitsFailed, 
		PlaybackFailed, 
		HangupFailed, 		 
		Linked, 
		DialStarted, ConferenceJoined, ConferenceLeft, ConferenceTalk, QueueJoined, QueueLeft, QueueJoinFailed, ConferenceMuted, ChannelListed, QueueBridged, AcdQueueBridged, AcdQueueFailed, AcdQueueJoined, AcdQueueLeft, AcdQueueBridgeFailed, DialFailed, Offered,
	}
	
	protected EventType eventType;	
	public EventType getEventType(){
		return eventType;
	}	
}
