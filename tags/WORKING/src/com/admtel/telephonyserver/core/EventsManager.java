package com.admtel.telephonyserver.core;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.mina.util.CopyOnWriteMap;

import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.EventListenerDefinition;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;

public class EventsManager implements EventListener, DefinitionChangeListener{
	
	CopyOnWriteMap<String, EventListener> listeners = new CopyOnWriteMap<String, EventListener>();
	
	MessageHandler messageHandler = new QueuedMessageHandler(){

		@Override
		public void onMessage(Object message) {
			Iterator<EventListener> it = listeners.values().iterator();
			while (it.hasNext()){
				it.next().onEvent((Event)message);
			}			
		}
		
	};
	
	private static class SingletonHolder{
		private static final EventsManager instance = new EventsManager();
	}
	
	public static EventsManager getInstance(){
		return SingletonHolder.instance;
	}
	
	public void addEventListener (String id, EventListener listener){
		listeners.put(id,listener);
	}
	public void removeEventListener(String id){
		listeners.remove(id);
	}

	@Override
	public boolean onEvent(Event event) {
		messageHandler.putMessage(event);
		return true;
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof EventListenerDefinition){
			EventListenerDefinition elDefinition = (EventListenerDefinition) definition;
			EventListener el = SmartClassLoader.createInstance(EventListener.class, elDefinition.getClassName());
			if (el != null){
				addEventListener(elDefinition.getId(),el);
			}
		}
		
	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		if (definition instanceof EventListenerDefinition){
			EventListenerDefinition elDefinition = (EventListenerDefinition) definition;
			removeEventListener(elDefinition.getId());
		}
		
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		if (newDefinition instanceof EventListenerDefinition &&
				oldDefinition instanceof EventListenerDefinition){
			definitionRemoved(oldDefinition);
			definitionAdded(newDefinition);
		}
		
	}	
}
