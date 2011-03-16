import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.PlayAndGetDigitsEndedEvent;
import com.admtel.telephonyserver.prompts.PromptBuilder;
import com.admtel.telephonyserver.events.AlertingEvent;
import com.admtel.telephonyserver.events.OfferedEvent;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.prompts.*;

public class  AdmCompleteExample extends Script {
	
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
	
	@Override
	protected void processPlaying_State(Event event){
		switch(event.getEventType()){
			case EventType.PlayAndGetDigitsEnded:
				PlayAndGetDigitsEndedEvent e = event
				println "################### Got digits "+ e.getDigits()
				a.joinConference "1234", true, false, false
			break
		}
	}
	
	@Override
	protected void processWaitingForCall_State(Event event){
		switch (event.getEventType()){
			case EventType.Offered:
				OfferedEvent e = event
				a = e.getChannel()
				if (a != null){					
					a.answer()
				}
				state = "Answering"
			break
			case EventType.Alerting:
				AlertingEvent e = event
				a = e.getChannel()
				if (a!=null){
				println "*********** outbound alerting on channel " + a
				}
				state = "Answering"
				break
		}
	}
	
	@Override
	protected void processAnswering_State(Event event){
		println "Start *************************************processAnswering_State*****************************************"
		switch (event.getEventType()){
			case EventType.Connected:
			a.setHangupAfter(50000);
			PromptBuilder pb = PromptBuilderFactory.getInstance().getPromptBuilder(a.getLanguage())
			def prompts = pb.currencyToPrompt(new BigDecimal(120.34))
			prompts += (pb.numberToPrompt(13245))
			prompts += (pb.dateToPrompt(new Date()))
			prompts += (pb.digitToPrompt("0123456789"))
			println prompts
			a.playAndGetDigits(10, (String[])prompts, 1000, "#")
			println "*******" + prompts
			state = "Playing"
			break
		}
		println "End *************************************processAnswering_State*****************************************"
	}
	
	@Override
	public void onStart(){
	}

}
