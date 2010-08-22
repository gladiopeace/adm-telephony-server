
import com.admtel.telephonyserver.core.*
import com.admtel.telephonyserver.events.*
import com.admtel.telephonyserver.radius.*;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.prompts.*;

import org.apache.log4j.Logger;

public class ConferenceScript extends Script {
	
	static Logger log = Logger.getLogger(ConferenceScript.class)

//	Channel a
//
//	
//	class JoinConference{
//		def JoinConference(a, confNo){
//			a.joinConference(confNo, false, false, false)
//		}
//	}
//	
//	class GetConferenceNumber{
//		def GetConferenceNumber(a){			
//			a.playAndGetDigits(10, "conference/conf-getconfno", 10000, "#")
//		}
//		
//		def onPlayAndGetDigitsEnded(PlayAndGetDigitsEndedEvent e){
//			new JoinConference(e.getChannel(), e.getDigits())
//		}
//	}
//	
//	class WaitForCall{
//		
//		def onInboundAlerting(InboundAlertingEvent evt){
//				evt.getChannel().answer()
//				this
//		}
//		def onAnswered(AnsweredEvent evt){
//			new GetConferenceNumber(evt.getChannel())
//		}
//	}
	
	
			
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
		println "on${event.getEventType()}" + "----" + currentState.toString()
		try{
			currentState = currentState."on${event.getEventType()}"(event)
		}
		catch (MissingMethodException e){
			
		}
	}		
	
	def currentState = [this] as conference.InitialState
}
