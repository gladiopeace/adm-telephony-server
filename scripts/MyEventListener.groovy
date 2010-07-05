import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;


class MyEventListener implements EventListener {

	static Logger log = Logger.getLogger(MyEventListener.class)
	@Override
	public boolean onEvent(Event event) {
		log.debug "****************" + event
		return false;
	}

}
