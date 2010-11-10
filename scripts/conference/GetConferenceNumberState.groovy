package conference;
import common.GState;
import com.admtel.telephonyserver.interfaces.Authorizer;
import com.admtel.telephonyserver.events.*;
import com.admtel.telephonyserver.radius.AuthorizeResult;
import com.admtel.telephonyserver.core.*;

class GetConferenceNumberState extends GState {
	
	int counter = 0;
	
	com.admtel.telephonyserver.interfaces.Authorizer authorizer;
	@Override
	public void onEnter() {
		authorizer = SmartClassLoader.getInstance().createInstance(Authorizer.class, script.getParameter("authorizer"));
		script.log.trace("Loaded authorizer $authorizer")
		script.channel.playAndGetDigits(10, "conference/conf-getconfno", 10000, "#")
	}
	
	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}
	def onPlayAndGetDigitsEnded (PlayAndGetDigitsEndedEvent e){
		
		script.log.trace("*************Got conference number ${e.getDigits()}")
		
		//authorizer.authorize(username, password, address, serviceType, calledStationId, callingStationId, loginIp, serviceNumber, routing, number)
		AuthorizeResult aResult = authorizer.authorize(e.getDigits(), "", "", 
			"Conference", script.channel.getChannelData().getCalledNumber(), script.channel.getChannelData().getCallerIdNumber(), "",
			 script.channel.getChannelData().getServiceNumber(), false, false);
		
		 script.log.trace("Authorize Result = *** " + aResult)
		 
		if (!aResult.getAuthorized()){
			return "PlayInvalidConference"
		}
		else{
			script.session.authorizeResult = aResult
			script.session.conferenceNumber = e.getDigits()
			return "GetConferencePin"
		}
	}
	
	def onPlayAndGetDigitsFailed(PlayAndGetDigitsFailedEvent e){
		counter ++;
		return null
	}
}
