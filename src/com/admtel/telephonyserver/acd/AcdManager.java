package com.admtel.telephonyserver.acd;

import java.util.List;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.acd.impl.AcdServiceImpl;
import com.admtel.telephonyserver.core.EventsManager;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.core.Switches;
import com.admtel.telephonyserver.core.Timers;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.requests.DialRequest;

public class AcdManager implements EventListener, TimerNotifiable {

	static Logger log = Logger.getLogger(AcdManager.class);	
	String acdServiceClassName = null;
	AcdService acdService;
		
	public AcdManager() {

	}
	
	public void init(){
		EventsManager.getInstance().addEventListener("ACD_Manager", this);
		if (acdServiceClassName == null){
			acdService = new AcdServiceImpl();
		}
		else{
			acdService = SmartClassLoader.getInstance().createInstanceI(AcdService.class, acdServiceClassName);
		}
		Timers.getInstance().startTimer(this, 1000, false, null);
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()){
			
		}
		return false;
	}

	@Override
	public boolean onTimer(Object data) {
		List<DialRequest> requests = acdService.getNextDial();
		for (DialRequest request:requests){
			Switches.getInstance().processRequest(request);
		}
		return false;
	}

}
