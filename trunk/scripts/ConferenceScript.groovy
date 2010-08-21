import com.admtel.telephonyserver.core.*
import com.admtel.telephonyserver.events.*
import com.admtel.telephonyserver.radius.*;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.prompts.*;

import org.apache.log4j.Logger;



class ConferenceScript extends Script {
	
	static Logger log = Logger.getLogger(ConferenceScript.class)

	
			
	@Override
	public String getDisplayStr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onStart(Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onTimer() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processEvent(Event event) {
		log.trace event
		
		println "********** " + event
		
		currentState event

	}		
	
	def joiningConference = {
		Event event = it
		switch (event.getEventType()) {
			case EventType.ConferenceJoined:
			 ConferenceJoinedEvent cje = event
			 println "********************" + cje
			break;
		}
	}
	
	def gettingConferenceNumber = {
		Event event = it
		switch (event.getEventType()){
			case EventType.PlayAndGetDigitsEnded:
				PlayAndGetDigitsEndedEvent e = event
				log.trace("User entered " + e.getDigits())
				e.getChannel().joinConference(e.getDigits(), false, false, false)
				currentState = joiningConference
			break;
		}
	}
	
	def waitForCall = {
		Event event = it
		println "*********** entered state waitForCall "
		switch (event.getEventType()){
			case EventType.InboundAlerting:
			 InboundAlertingEvent e = event
			 println "*********** answering " + e.getChannel()
			 e.getChannel().answer()
			break;
			case EventType.Answered:
				Channel c = event.getChannel()
				c.playAndGetDigits(10, "conference/conf-getconfno", 10000, "#")
				currentState = gettingConferenceNumber
			break;
		}
	}
	
	def currentState = waitForCall
}
