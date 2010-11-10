package conference;

import common.GState;
import com.admtel.telephonyserver.events.AnsweredEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;

class WaitForCallState extends GState {

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}
	def onInboundAlerting (InboundAlertingEvent e){
		e.getChannel().answer()
		return null
	}
	def onAnswered(AnsweredEvent e){
		return "GetConferenceNumber"
	}
}
