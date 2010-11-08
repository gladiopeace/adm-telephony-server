package common
import com.admtel.telephonyserver.core.*;
import com.admtel.telephonyserver.events.*;
import com.admtel.telephonyserver.events.Event.EventType;
import org.apache.log4j.Logger;

class GScript extends com.admtel.telephonyserver.core.Script{
	
	static Logger log = Logger.getLogger(GScript.class)
	
	GState currentState
	String currentStateStr
	
	String baseDir;
	
	Channel channel
	
	Expando session

	private GState newState(String stateName){
		String str = "./"+baseDir+"/"+stateName+"State.groovy"
		
		//GState result = (str as Class).newInstance()
		GState result = SmartClassLoader.getInstance().createInstance(GState.class, str)
		if (result != null){
			result.setScript this
		}
		return result
	}
	
	public GScript(String baseDir, String stateStr){
		session = new Expando()
		this.baseDir = baseDir
		currentStateStr = stateStr
		currentState = newState(currentStateStr)
	}
		
	@Override
	public String getDisplayStr() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onStop() {
	}
	
	@Override
	protected void onTimer() {		
	}
	
	@Override
	protected void processEvent(Event event) {		
		log.trace(" <<<<<<<< processEvent event : ${event} - currentState : ${currentState}")
		switch (event.getEventType()){
			case Event.EventType.InboundAlerting:
				InboundAlertingEvent iae = event
				channel = iae.getChannel()
			break;
		}
		String nextStateStr = callEventHandler(event)
		
		if (nextStateStr != currentStateStr && nextStateStr != null){
			try{
				GState nextState = newState(nextStateStr)
				if (nextState != null){
					nextState.setScript this
					nextState.onEnter()
					currentState.onExit()
					currentState = nextState
				}
			}
			catch (Exception e){
				e.printStackTrace();
				//TODO log
			}
		}
		log.trace(" >>>>>>>> processEvent event : ${event} - currentState : ${currentState}")
	}
	
	
	private String callEventHandler (Event event){
		String result = null;
		if (currentState != null){
			try{
				result = currentState.onEvent (event)
				if (result == null || result.isEmpty()){
					result = currentState."on${event.getEventType()}"(event)
				}
			}
			catch (MissingMethodException e){
				//TODO log info	
			}
			return result;
		}
	}
}
