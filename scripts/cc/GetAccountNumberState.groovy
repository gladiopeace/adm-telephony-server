package cc;
import common.GState;
import com.admtel.telephonyserver.interfaces.Authorizer;
import com.admtel.telephonyserver.events.PlayAndGetDigitsEndedEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsFailedEvent;
import com.admtel.telephonyserver.radius.AuthorizeResult;
import com.admtel.telephonyserver.core.SmartClassLoader;

class WaitForCallState extends GState {

	int counter = 0;
	
	com.admtel.telephonyserver.interfaces.Authorizer authorizer;
	@Override
	public void onEnter() {
		authorizer = SmartClassLoader.getInstance().createInstance(Authorizer.class, script.getParameter("authorizer"));
		script.channel.playAndGetDigits(10, "callingcard/accountnum", 5000, "#")
	
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}
	def onPlayAndGetDigitsEnded (PlayAndGetDigitsEndedEvent e){
		counter ++
		AuthorizeResult aResult = authorizer.authorize(e.getDigits(), "", "", "login-user", "", "", "", "", false, false);
		
		if (aResult.getAuthorized()){
			script.session.accountNumber = aResult.getUserName()
			script.session.authorizer = authorizer
			script.session.credit = aResult.getCredit()
			return "PlayCredit"
		}
		if (counter < 3){
			e.getChannel().playAndGetDigits(10, "callingcard/accountnum", 5000, "#")
			
			return null
		}
		return "Hangup"
	}
	def onPlayAndGetDigitsFailed(PlayAndGetDigitsFailedEvent e){
		counter ++;
		return null
	}
}
