package cc;
import common.GState;
import com.admtel.telephonyserver.interfaces.Authorizer;
import com.admtel.telephonyserver.events.PlayAndGetDigitsEndedEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsFailedEvent;
import com.admtel.telephonyserver.radius.AuthorizeResult;

class GetPhoneNumberState extends GState {
	
	int counter = 0;
	
	
	@Override
	public void onEnter() {
		
		script.channel.playAndGetDigits(10, "callingcard/phonenum", 10000, "#")
	}
	
	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}
	def onPlayAndGetDigitsEnded (PlayAndGetDigitsEndedEvent e){
		counter ++
		AuthorizeResult aResult = script.session.authorizer.authorize(e.getDigits(), "", "", "Login-User", e.getDigits(), script.channel.getCallingStationId(), "", "", false, false);
		if (aResult.getAuthorized() && aResult.getRoutes()?.size() > 0){
			script.session.routes = aResult.getRoutes()
			return "Dial"
		}
		if (counter < 3){
			e.getChannel().playAndGetDigits(10, "callingcard/phonenum", 10000, "#")
			return null
		}
		return "Hangup"
	}
	def onPlayAndGetDigitsFailed(PlayAndGetDigitsFailedEvent e){
		counter ++;
		return null
	}
}
