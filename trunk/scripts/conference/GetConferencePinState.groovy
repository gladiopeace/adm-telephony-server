package conference;

import common.GState;;
import com.admtel.telephonyserver.events.*;

class GetConferencePinState extends GState {

	int counter = 0;
	
	@Override
	public void onEnter() {		
		script.channel.playAndGetDigits(4,"conference/conf-getpin", 5000,"")	
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}
	def onPlayAndGetDigitsEnded(PlayAndGetDigitsEndedEvent e){
		
		script.log.trace ("GetConferencePin got ${e.getDigits()}")
		
		if (e.getDigits() == script.session.authorizeResult.get("admin-pass")){
			script.session.participantType = "Admin"
			return "JoinConference"
		}
		else
		if (e.getDigits() == script.session.authorizeResult.get("manager-pass")){
			script.session.participantType = "Manager"
			return "JoinConference"
			
		}
		else if (e.getDigits() == script.authorizeResult.get("user-pass")){
				script.session.participantType = "User"
				return "JoinConference"
		}
		return "PlayInvalidPin"
	}
	def onPlayAndGetDigitsFailed(PlayAndGetDigitsFailedEvent e){
		return "PlayInvalidPin"
	}
}
