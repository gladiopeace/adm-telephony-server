package cc;

import com.admtel.telephonyserver.events.*;
import com.admtel.telephonyserver.events.DialFailedEvent.Cause;
import common.GState;

class HangupState extends GState {

	@Override
	public void onEnter() {
		String prompt = "callingcard/cannot-complete-as-dialed"
		switch(script.session.dialFailedCause){
			case Cause.InvalidNumber:
			prompt = "callingcard/badphone"
			break;
			case Cause.NoAnswer:
			prompt = "callingcard/noanswer"
			break;
		}
		script.channel.playback(prompt, "")
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}	
	def onPlaybackEnded (PlaybackEndedEvent e){
		"GetPhoneNumber"
	}
}
