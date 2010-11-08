package cc;
import com.admtel.telephonyserver.core.DisconnectCode;
import common.GState;
import com.admtel.telephonyserver.events.AnsweredEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;

class HangupState extends GState {

	@Override
	public void onEnter() {
		script.channel.hangup(DisconnectCode.Normal)
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}	
}
