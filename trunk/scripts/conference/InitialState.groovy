package conference

import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.*
import com.admtel.telephonyserver.events.*
import com.admtel.telephonyserver.radius.*;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.prompts.*;

import org.apache.log4j.Logger;


/////////////////////////////////////////////////////////////////////////////
public class InitialState{
	
	ConferenceScript script
	
	InitialState (script){
		this.script = script
	}
	def onInboundAlerting (InboundAlertingEvent evt){
		evt.getChannel().answer()
		this
	}
	def onAnswered(AnsweredEvent e){
		[script, e.getChannel()] as GetConferenceNumber
	}
}
/////////////////////////////////////////////////////////////////////////////
public class InvalidConference{
	
	ConferenceScript script
	
	InvalidConference(script, channel){
		this.script = script
		channel.playback("conference/conf-invalid", "*")
	}
	def onPlaybackEnded(PlaybackEndedEvent e){
		[script, e.getChannel()] as GetConferenceNumber
	}
}
/////////////////////////////////////////////////////////////////////////////
public class GetConferenceNumber{
	
	static final Logger log = Logger.getLogger(GetConferenceNumber.class);
	
	ConferenceScript script
	GetConferenceNumber(script, channel){
		this.script = script
		channel.playAndGetDigits(10, "conference/conf-getconfno", 10000, "#")
	}
	def onPlayAndGetDigitsEnded (PlayAndGetDigitsEndedEvent e){
		
		 script.authorizeResult = Radius.authorize(e.getChannel(), e.getDigits(), 
			"", "", "Conference", e.getChannel().getCallingStationId(), 
			e.getChannel().getCalledStationId(), false, true)
		
		script.conferenceNumber = e.getDigits()
		log.trace(script.authorizeResult)
		println ("***************** " + script.authorizeResult)
		if (!script.authorizeResult.getAuthorized()){
			[script, e.getChannel()] as InvalidConference
		}
		else{
			[script, e.getChannel()] as GetConferencePin
		}
//		script.conferenceDTO = script.getConference(e.getDigits())
//		if (script.conferenceDTO == null){
//			[script, e.getChannel()] as InvalidConference
//		}
//		else{
//			[script, e.getChannel()] as GetConferencePin
//		}
		
	}
	def onPlayAndGetDigitsFailed(PlayAndGetDigitsFailedEvent e){
		[script, e.getChannel()] as InvalidConference
	}
}
/////////////////////////////////////////////////////////////////////////////
public class GetConferencePin{
	
	static final Logger log = Logger.getLogger(GetConferencePin.class);
	
	ConferenceScript script
	
	GetConferencePin(script, channel){
		this.script = script
		channel.playAndGetDigits(4,"conference/conf-getpin", 5000,"")
	}
	
	def onPlayAndGetDigitsEnded(PlayAndGetDigitsEndedEvent e){
		
		log.trace ("GetConferencePin got ${e.getDigits()}")
		
		if (e.getDigits() == script.authorizeResult.get("admin-pass")){
			[script, e.getChannel()] as JoinConference
		}
		else
		if (e.getDigits() == script.authorizeResult.get("manager-pass")){
			[script, e.getChannel(), false] as JoinQueue
		}
		else if (e.getDigits() == script.authorizeResult.get("user-pass")){
				[script, e.getChannel()] as InvalidConferencePin
		}
		else{
			[script, e.getChannel()] as InvalidConferencePin 
		}
	}
	def onPlayAndGetDigitsFailed(PlayAndGetDigitsFailedEvent e){
		[script, e.getChannel()] as InvalidConferencePin
	}
}

public class InvalidConferencePin{
	ConferenceScript script
	
	InvalidConferencePin(script, channel){
		this.script = script
		channel.playback("conference/conf-invalidpin","*")
	}
	def onPlaybackEnded(PlaybackEndedEvent e){
		[script, e.getChannel()] as GetConferencePin
	}
}

public class JoinQueue{
	ConferenceScript script
	
	JoinQueue(script, channel, isAgent){
		this.script = script
		channel.queue (script.conferenceNumber, isAgent)
	}
	def onQueueLeft (QueueLeftEvent e){
		if (!e.isAgent()){
			[script, e.getChannel()] as JoinConference
		}
	}
}

/////////////////////////////////////////////////////////////////////////////
public class JoinConference{
	
	static final Logger log = Logger.getLogger(JoinConference.class);
	
	ConferenceScript script
	
	JoinConference(script, channel){
		this.script = script
		channel.joinConference(script.conferenceNumber, false, false, false)
	}
//	def onConferenceJoined(ConferenceJoinedEvent e){
//		
//	}
}
/////////////////////////////////////////////////////////////////////////////