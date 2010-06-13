package com.admtel.telephonyserver.core;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;

public class EventsManager implements EventListener{
	
	CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<EventListener>();
	private static class SingletonHolder{
		private static final EventsManager instance = new EventsManager();
	}
	
	public static EventsManager getInstance(){
		return SingletonHolder.instance;
	}
	
	public void addEventListener (EventListener listener){
		listeners.add(listener);
	}
	public void removeEventListener(EventListener listener){
		listeners.remove(listener);
	}

	@Override
	public boolean onEvent(Event event) {
		Iterator<EventListener> it = listeners.iterator();
		while (it.hasNext()){
			it.next().onEvent(event);
		}
		return true;
	}
	
}
