package com.admtel.telephonyserver.core;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;
import com.admtel.telephonyserver.events.OutboundAlertingEvent;
import com.admtel.telephonyserver.interfaces.EventListener;

import com.admtel.telephonyserver.interfaces.Authorizer;
import com.admtel.telephonyserver.radius.AuthorizeResult;
import com.admtel.telephonyserver.registrar.UserLocation;

public abstract class Script implements EventListener{

	static Logger log = Logger.getLogger(Script.class);

	enum ScriptState {
		Running, Stopped
	};

	String id;
	
	ScriptState scriptState = ScriptState.Running;

	Map<String, String> parameters;
	
	public Map<String, String> getParameters(){
		return parameters;
	}
	public String getParameter(String key){
		if (parameters != null){
			return parameters.get(key);
		}
		return null;
	}
	public void setParameters (Map<String, String> parameters){
		this.parameters = parameters;
	}
	public ScriptState getState() {
		return scriptState;
	}

	public Script() {
		id = UUID.randomUUID().toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}
	public String getId(){
		return id;
	}
	public String dump() {
		return dump(0);
	}

	public String dump(int level) {
		String result = this.getClass().getSimpleName()
				+ String.format(":State(%s)", scriptState);
		return result;
	}


	final public boolean onEvent(Event event) {
		
		try{
		log.trace(this + ", got event " + event);		
		switch (event.getEventType()) {
		case InboundAlerting: {
			InboundAlertingEvent ie = (InboundAlertingEvent) event;
		}
			break;
		case OutboundAlerting:{
			OutboundAlertingEvent oa = (OutboundAlertingEvent)event;
		}
		break;
		case Hangup: {
			HangupEvent he = (HangupEvent) event;
			log.debug(this + ", got hangup, cleared channel");
		}
			break;
		case DialStarted: {
			DialStartedEvent dse = (DialStartedEvent) event;
			// Add the dialed channel to the list of channels, and add us to the
			// listeners
			log.debug(this+", Dial Started ....");
			if (dse.getDialedChannel() != null) {
				log.debug(String.format("%s, DialStarted : %s--->%s", this, dse
						.getChannel().getId(), dse.getDialedChannel().getId()));
				dse.getDialedChannel().addEventListener(this);
			}
		}
			break;
		}

		processEvent(event);

		}
		catch (Exception e){
			log.fatal(this+", " + e.getMessage(), e);
		}
		return true;
	}
	//Registrar functions
	public UserLocation find(String user){
		return Registrar.getInstance().find(user);
	}
	
	public abstract String getDisplayStr();

	protected abstract void processEvent(Event event);

	protected abstract void onTimer();

	protected abstract void onStop();
	
	protected abstract void onCreate();
}
