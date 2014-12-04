import com.admtel.telephonyserver.core.*;
import com.admtel.telephonyserver.events.*;

class PlayAndGetDigitsTest extends Script {

	@Override
	public String getDisplayStr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onStart() {
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
		currentState event

	}

	@Override
	protected void onCreate(){
	
	}
	
	def waitForCall={
			Event event = it
			switch (event.getEventType()){
			case Event.EventType.Offered:
				OfferedEvent iae = event
				iae.getChannel().answer()
				currentState = answering
				break;
			}
	}
	def answering={
		Event event = it
		switch (event.getEventType()){
			case Event.EventType.Connected:
				ConnectedEvent ae = event
			String[] prompts =["ivr/8000/ivr-sample_submenu",
				"ivr/ivr-account_number",
				"ivr/ivr-sample_submenu"]
				ae.getChannel().playAndGetDigits(10, prompts, 30000, "#")
				currentState = gettingDigits
			break;
		}
	}
	
	def gettingDigits = {
		Event event = it
		switch (event.getEventType()){
			case Event.EventType.PlayAndGetDigitsEnded:
				PlayAndGetDigitsEndedEvent pde = event
				println "********** Got digits " + pde.getDigits()
			break;
		}	
	}
	
	def currentState = waitForCall
}
