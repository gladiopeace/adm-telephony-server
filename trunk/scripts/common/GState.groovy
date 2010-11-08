package common
import com.admtel.telephonyserver.events.Event;

abstract class GState {
	
	GScript script;

	public void setScript(GScript script){
		this.script = script;
	}	
	abstract public void onEnter();
	abstract public void onExit();
	public void onEvent(Event event){
		//TODO log event
		return null;
	}	
}
