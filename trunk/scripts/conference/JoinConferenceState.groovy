package conference;

import common.GState;
import com.admtel.telephonyserver.events.*;

class JoinConferenceState extends GState {

	@Override
	public void onEnter() {
		script.channel.joinConference(script.session.conferenceNumber, false, false, false)
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}
	
}
