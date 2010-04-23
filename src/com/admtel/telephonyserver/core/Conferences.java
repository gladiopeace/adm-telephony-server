package com.admtel.telephonyserver.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.admtel.telephonyserver.interfaces.TimerNotifiable;

public class Conferences implements TimerNotifiable{
	
	Map<String, Conference> conferences = new HashMap<String, Conference>();
	Map<String, Conference> synchronizedConferences = Collections.synchronizedMap(conferences);
	private Conferences(){
		Timers.getInstance().startTimer(this, 5000, true, null);
	}
	
	private static class SingletonHolder{
		private static final Conferences instance = new Conferences();
	}
	
	public static Conferences getInstance(){
		return SingletonHolder.instance;
	}
	
	public Conference getConferenceById(String id){
		return conferences.get(id);
	}
	synchronized public Conference createConference(String id){
		Conference c = conferences.get(id);
		if (c == null){
			c = new Conference(id);
			conferences.put(id, c);
		}
		return c;
	}
	public boolean joinConference(String conferenceId, Channel channel){
		Conference c = createConference(conferenceId);
		if (c != null){
			c.addChannel(channel);
			return true;
		}
		return false;
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
}
