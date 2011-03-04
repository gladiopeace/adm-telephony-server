package com.admtel.telephonyserver.events;

public abstract class Event {
	
	
	@Override
	public String toString() {
		return (eventType != null ? "eventType=" + eventType : "");
	}
	public enum EventType {		
		Disconnected,
		Connected,
		PlaybackStarted,
		PlaybackEnded,
		PlayAndGetDigitsStarted,
		PlayAndGetDigitsEnded,
		Alerting, 		
		DTMF, 
		AnswerFailed, 
		PlayAndGetDigitsFailed, 
		PlaybackFailed, 
		HangupFailed, 
		Linked, 
		DialStarted, 
		ConferenceJoined, 
		ConferenceLeft, 
		ConferenceTalk, 
		QueueJoined, 
		QueueLeft, 
		QueueJoinFailed, 
		ConferenceMuted, 
		ChannelListed, 
		QueueBridged, 
		AcdQueueBridged, 
		AcdQueueFailed, 
		AcdQueueJoined, 
		AcdQueueLeft, 
		AcdQueueBridgeFailed, 
		DialFailed, 
		Offered, 
		Registered, 
		Unregistered,
	}
	
	protected EventType eventType;	
	public EventType getEventType(){
		return eventType;
	}	
}
