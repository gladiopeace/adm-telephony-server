package conference;

import common.GState;
import com.admtel.telephonyserver.events.*;

class PlayInvalidPinState extends GState {

	@Override
	public void onEnter() {
		script.channel.playback("conference/conf-invalidpin","")

	}
	
	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}
	def onPlaybackEnded(PlaybackEndedEvent event){
		return "GetConferencePin"
	}
	def onPlaybackFailed(PlaybackFailedEvent event){
		return "GetConferencePin"
	}
}
