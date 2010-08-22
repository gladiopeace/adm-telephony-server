package conference

import com.admtel.telephonyserver.core.*
import com.admtel.telephonyserver.events.*
import com.admtel.telephonyserver.radius.*;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.prompts.*;

import org.apache.log4j.Logger;

public class InitialState{
	
	Script script
	
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

public class GetConferenceNumber{

	Script script
	GetConferenceNumber(script, channel){
		this.script = script
		channel.playAndGetDigits(10, "conference/conf-getconfno", 10000, "#")
	}
	def onPlayAndGetDigitsEnded (PlayAndGetDigitsEndedEvent e){
		[script, e.getChannel(), e.getDigits()] as JoinConference
	}
	
}

public class JoinConference{
	Script script
	JoinConference(script, channel, conference){
		this.script = script
		channel.joinConference(conference, false, false, false)
	}
	def onConferenceJoined(ConferenceJoinedEvent e){
		e.getChannel().hangup(DisconnectCode.Normal)
	}
}