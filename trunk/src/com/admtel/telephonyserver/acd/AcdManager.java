package com.admtel.telephonyserver.acd;

import java.util.List;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.acd.impl.AcdDataProviderImpl;
import com.admtel.telephonyserver.acd.impl.AcdServiceImpl;
import com.admtel.telephonyserver.core.EventsManager;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.core.Switches;
import com.admtel.telephonyserver.core.Timers;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.requests.DialRequest;

public class AcdManager implements EventListener, TimerNotifiable {

	static Logger log = Logger.getLogger(AcdManager.class);
	public AcdService acdService;	
	public int timeout = 1000; 
	
	public AcdManager() {

	}

	public void init() {
		EventsManager.getInstance().addEventListener("ACD_Manager", this);
		log.trace("Initializing ACD Manager");
		Timers.getInstance().startTimer(this, timeout, false, null);
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()) {
		case Hangup: {
			HangupEvent he = (HangupEvent) event;
			if (acdService.containsChannel(he.getChannel().getUniqueId())) {
				acdService.unqueueChannel(he.getChannel().getUniqueId());
			}

		}
			break;
		}
		return false;
	}

	@Override
	public boolean onTimer(Object data) {
		List<DialRequest> dialRequests = acdService.getNextDial();
		for (DialRequest dialRequest : dialRequests) {
			Switches.getInstance().processRequest(dialRequest);
		}
		return false;
	}

}
