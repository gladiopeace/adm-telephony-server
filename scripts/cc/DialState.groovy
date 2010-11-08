package cc;
import common.GState;
import com.admtel.telephonyserver.events.*;


class DialState extends GState {

	@Override
	public void onEnter() {
		script.channel.dial("sip:6000", 10000)
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}	
	def onDialStarted(DialStartedEvent event){
		script.log.trace("Dial started ")
	}
	def onDialFailed(DialFailedEvent event){
		script.log.trace("Dial failed *************************")
	}
	def onHangup(HangupEvent e){
		if (e.getChannel() != script.channel){			
			return "GetPhoneNumber"
		}
		"Hangup"
	}
}
