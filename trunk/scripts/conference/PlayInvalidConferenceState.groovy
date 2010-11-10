package conference;

import common.GState;
import com.admtel.telephonyserver.events.*;

class PlayInvalidConferenceState extends GState {

	@Override
	public void onEnter() {
		script.channel.playback("conference/conf-invalid","")

	}
	
	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}
	def onPlaybackEnded(PlaybackEndedEvent event){
		return "GetConferenceNumber"
	}
	def onPlaybackFailed(PlaybackFailedEvent event){
		return "GetConferenceNumber"
	}
}
