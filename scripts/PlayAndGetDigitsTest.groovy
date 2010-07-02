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
				break;
			}
	}
	
	def currentState = waitForCall
}
