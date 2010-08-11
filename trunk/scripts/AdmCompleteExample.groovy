import com.admtel.telephonyserver.prompts.PromptBuilder
import com.admtel.telephonyserver.core.*
import com.admtel.telephonyserver.events.*
import com.admtel.telephonyserver.radius.*;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.prompts.*;

class AdmCompleteExample extends Script {
	
	def state="WaitingForCall";
	Channel a;
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onTimer() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processEvent(Event event) {
		println "##### Script " + this +": got event : " + event
		switch (state){
			case "WaitingForCall":
				processWaitingForCall_State(event)
			break
			case "Answering":
				processAnswering_State(event)
			break
			case "Playing":
				processPlaying_State(event)
			break
		}

	}
	
	@Override
	public String getDisplayStr() {
		// TODO Auto-generated method stub
		return null;
	}
	void processPlaying_State(Event event){
		switch(event.getEventType()){
			case EventType.PlayAndGetDigitsEnded:
				PlayAndGetDigitsEndedEvent e = event
				println "################### Got digits "+ e.getDigits()
				a.joinConference "1234", true, false, false
			break
		}
	}
	@Override
	public void onStart (data){
		
	}
	void processWaitingForCall_State(Event event){
		switch (event.getEventType()){
			case EventType.InboundAlerting:
				InboundAlertingEvent e = event
				a = e.getChannel()
				if (a != null){
					println "********inbound alerting on channel " + a;
					AuthorizeResult ar = Radius.authorize(a, "selzein", "1234", "", "", "9613820376", true, true)
					if (ar != null){
						println "*********"+ar.getAuthorized()+", " + ar.getAllowedTime()
					}
					a.answer()
				}
				state = "Answering"
			break
			case EventType.OutboundAlerting:
				OutboundAlertingEvent e = event
				a = e.getChannel()
				if (a!=null){
				println "*********** outbound alerting on channel " + a
				}
				state = "Answering"
				break
		}
	}
	void processAnswering_State(Event event){
		switch (event.getEventType()){
			case EventType.Answered:
			PromptBuilder pb = PromptBuilderFactory.getInstance().getPromptBuilder(a.getLanguage())
			def prompts = pb.currencyToPrompt(new BigDecimal(120.34))
			prompts += (pb.numberToPrompt(-245))
			a.playAndGetDigits(10, (String[])prompts, 1000, "#")
			
//			String[] prompts=	["/us/callie/ivr/8000/ivr-sample_submenu",
//			"/usr/local/freeswitch/sounds/en/us/callie/ivr/8000/ivr-account_number",
//			"/usr/local/freeswitch/sounds/en/us/callie/ivr/8000/ivr-sample_submenu"]
//			a.playAndGetDigits(10, prompts, 10000, "#")
			state = "Playing"
			break
		}
	}

}
