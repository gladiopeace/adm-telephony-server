package com.admtel.telephonyserver.scripts;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.OfferedEvent;

public class InboundTestScript2 extends Script {

	static private Logger log = Logger.getLogger(InboundTestScript2.class);
	@Override
	public String getDisplayStr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void processEvent(Event event) {
		log.trace(event);
		switch (event.getEventType()){
		case Offered:
		{
			OfferedEvent oe = (OfferedEvent) event;
			oe.getChannel().answer();
		}
			break;
		}

	}

	@Override
	protected void onTimer() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}


}
