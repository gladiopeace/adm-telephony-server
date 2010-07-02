import com.admtel.telephonyserver.events.InboundAlertingEvent;

import com.admtel.telephonyserver.events.Event.EventType;

import com.admtel.telephonyserver.core.Script;

import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.registrar.UserLocation;

class SoftSwitch extends Script {
	
	
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
		currentState(event)
		
	}

	
	// Processing event
	
	def waitForCall = {
			Event evt = it
			switch (evt.getEventType()){
			case EventType.InboundAlerting:
				InboundAlertingEvent iae = evt;
				println "***** looking for user " + iae.getCalledIdNumber();
				UserLocation userLocation = this.find(iae.getCalledIdNumber());
				if (userLocation){
					iae.getChannel().dial(userLocation, 10000)
				}
				break;
			}
	}
	
	def currentState = waitForCall
}
