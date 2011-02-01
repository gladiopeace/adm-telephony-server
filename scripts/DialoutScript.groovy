import com.admtel.telephonyserver.core.*;
import com.admtel.telephonyserver.events.*;
import com.admtel.telephonyserver.events.Event.EventType;

class DialoutScript extends Script {
	
	def state = "Idle"
	Channel a
	Channel b
	String displayStr
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
			case "Idle":
			 processIdle_State(event)
			break
			case "Answering":
			 processAnswering_State(event)
			break
			case "Dialing":
			 processDialing_State(event)
			break
			case "Connected":
			 processConnected_State(event)
			break
		}

	}
	
	@Override
	public String getDisplayStr() {
		return displayStr
	}
	
	@Override
	public void onStart (data){
		
	}
	
	protected void processIdle_State(Event event){
		switch (event.getEventType()){
			case EventType.InboundAlerting:
				InboundAlertingEvent e = event
				a = e.getChannel()
				a.answer()
				state = "Answering"
				displayStr = "Issued answer command"
			break;
		}
	}
	protected void processAnswering_State(Event event){
		switch (event.getEventType()){
			case EventType.Answered:
			 a.dial("sip:1008", 10000)
			state = "Dialing"
			displayStr = "Dialed sip:hassan"
			break
		}
	}
	protected void processDialing_State(Event event){
		switch (event.getEventType()){
			case EventType.DialStarted:
			 DialStartedEvent dse = event
			println "*************** channel " + dse.getChannel().getId()+"dialed "+dse.getDialedChannel().getId();
			b = dse.getDialedChannel()
			break
			case EventType.Answered:
			 AnsweredEvent ae = event
			println "************ channel "+ ae.getChannel() +", was answered"
			if (ae.getChannel() == b){
				println "*********** channel b answered"
				state = "Connected"
				displayStr = "Channel "+a.getId() + "is in speech with channel "+b.getId()
			}
			break
		}
	}
	protected void processConnected_State(Event event){
		
	}
	@Override
	protected void onStart(){
	
	}
}
