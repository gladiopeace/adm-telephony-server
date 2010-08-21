package com.admtel.telephonyserver.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.admtel.telephonyserver.events.ConferenceJoinedEvent;
import com.admtel.telephonyserver.events.ConferenceLeftEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.radius.RadiusServers;

public class ConferenceManager implements TimerNotifiable, EventListener{
	
	Map<String, Conference> conferences = new HashMap<String, Conference>();
	Map<String, Conference> synchronizedConferences = Collections.synchronizedMap(conferences);
	private ConferenceManager(){
		Timers.getInstance().startTimer(this, 5000, true, null);
	}
		
	private static class SingletonHolder {
		private final static ConferenceManager instance = new ConferenceManager();
	}
	
	public static ConferenceManager getInstance(){
		return SingletonHolder.instance;
	}
	public Conference getConferenceById(String id){
		return conferences.get(id);
	}

	@Override
	public boolean onTimer(Object data) {
		return true;
	}
	
	public Collection<Conference> getAll(){
		return conferences.values();
	}

	public String getSwitchId() {
		return null;
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()){
		case ConferenceJoined:{
			ConferenceJoinedEvent cje = (ConferenceJoinedEvent) event;
			Conference c = conferences.get(cje.getConferenceId());
			
			RadiusServers.getInstance().accountingStart(cje.getChannel(), c, c.getParticipant(cje.getChannel()));
			
			if (c == null){
				c = new Conference (cje.getConferenceId());
				synchronizedConferences.put(cje.getConferenceId(), c);
			}
			c.onConferenceJoined (cje);
		}
			break;
		case ConferenceLeft:{
			ConferenceLeftEvent cle = (ConferenceLeftEvent) event;
			Conference c = conferences.get(cle.getConferenceId());
			
			RadiusServers.getInstance().accountingStop(cle.getChannel(), c, c.getParticipant(cle.getChannel()));
			
			if (c != null){
				c.onConferenceLeft(cle);
				if (c.getParcitipantsCount() == 0){
					synchronizedConferences.remove(c.getId());
				}
			}
		}
			break;
		}
		return false;
	}
}
