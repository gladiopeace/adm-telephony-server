package com.admtel.telephonyserver.eventlisteners;

import org.apache.log4j.Logger;


import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;

public class SimpleEventListener implements EventListener{
	static Logger log = Logger.getLogger(SimpleEventListener.class);
	
	@Override
	public boolean onEvent(Event event) {
		return true;
	}	
}