package conference;

import common.GState;
import com.admtel.telephonyserver.events.*;

class JoinQueueState extends GState {

	@Override
	public void onEnter() {
		script.channel.queue(script.session.conferenceNumber, false)

	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}
	
}
