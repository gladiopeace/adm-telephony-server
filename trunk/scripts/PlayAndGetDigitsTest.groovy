import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.events.*;
import com.admtel.telephonyserver.core.Channel;


class PlayAndGetDigitsTest extends Script {

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
		currentState event

	}

	def waitForCall={
			Event event = it
			switch (event.getEventType()){
			case Event.EventType.InboundAlerting:
				InboundAlertingEvent iae = event
				iae.getChannel().answer()
				currentState = answering
				break;
			}
	}
	def answering={
		Event event = it
		switch (event.getEventType()){
			case Event.EventType.Answered:
				AnsweredEvent ae = event
			String[] prompts =["ivr/8000/ivr-sample_submenu",
				"ivr/ivr-account_number",
				"ivr/ivr-sample_submenu"]
				ae.getChannel().playAndGetDigits(10, prompts, 10000, "#")
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
