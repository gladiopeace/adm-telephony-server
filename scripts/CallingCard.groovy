import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.InboundAlertingEvent;

import com.admtel.telephonyserver.events.Event.EventType;

import com.admtel.telephonyserver.core.Script;

import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.registrar.UserLocation;
import com.admtel.telephonyserver.radius.*;
import org.apache.log4j.Logger;

class CallingCard extends Script {
	static final Logger log = Logger.getLogger(CallingCard.class)
		
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
		try{
			log.debug "CallingCardScript State = ${currentState.toString()} on${event.getEventType()}"
			
			def nextState = currentState."on${event.getEventType()}"(event)
			if (nextState != null)
				currentState = nextState
		}
		catch (MissingMethodException e){
			
		}
	}		
	
	def currentState = [this] as callingcard.InitialState
}
