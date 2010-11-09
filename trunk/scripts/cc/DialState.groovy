package cc;
import common.GState;
import com.admtel.telephonyserver.events.*;


class DialState extends GState {

	int count = 0;
	@Override
	public void onEnter() {		
		script.channel.dial(script.session.routes[count], 10000)
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}	
	def onDialStarted(DialStartedEvent event){
		script.log.trace("Dial started ***********************")
	}
	def onDialFailed(DialFailedEvent event){
		count ++;
		if (script.session.routes.size()+1 > count){
			script.channel.dial(script.session.routes[count],10000)
			return null;
		}
		script.session.dialFailedCause = event.getCause()
		return "PlayDialFailed"
	}
	def onHangup(HangupEvent e){
		if (e.getChannel() != script.channel){			
			return "GetPhoneNumber"
		}
		"Hangup"
	}
}
