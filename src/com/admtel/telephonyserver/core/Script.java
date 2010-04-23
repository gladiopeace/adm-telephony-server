package com.admtel.telephonyserver.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;
import com.admtel.telephonyserver.events.OutboundAlertingEvent;
import com.admtel.telephonyserver.interfaces.EventListener;

public abstract class Script implements EventListener {

	static Logger log = Logger.getLogger(Script.class);

	enum ScriptState {
		Running, Stopped
	};

	String id;
	
	ScriptState scriptState = ScriptState.Running;

	List<Channel> channels = new ArrayList<Channel>();

	public ScriptState getState() {
		return scriptState;
	}

	public Script() {
		id = UUID.randomUUID().toString();
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

	public List<Channel> getChannels() {
		return channels;
	}

	final public boolean onEvent(Event event) {
		log.trace("Script " + this + ", got event " + event);

		switch (event.getEventType()) {
		case InboundAlerting: {
			InboundAlertingEvent ie = (InboundAlertingEvent) event;
			channels.add(ie.getChannel());
		}
			break;
		case OutboundAlerting:{
			OutboundAlertingEvent oa = (OutboundAlertingEvent)event;
			channels.add(oa.getChannel());
		}
		break;
		case Hangup: {
			HangupEvent he = (HangupEvent) event;
			channels.remove(he.getChannel());
			log.debug("Script " + this + ", got hangup, cleared channel");
		}
			break;
		case DialStarted: {
			DialStartedEvent dse = (DialStartedEvent) event;
			// Add the dialed channel to the list of channels, and add us to the
			// listeners
			log.debug("Dial Started ....");
			if (dse.getDialedChannel() != null) {
				log.debug(String.format("DialStarted : %s--->%s", dse
						.getChannel().getId(), dse.getDialedChannel().getId()));
				dse.getDialedChannel().addEventListener(this);
				channels.add(dse.getDialedChannel());
			}
		}
			break;
		}

		processEvent(event);

		if (channels.size() == 0) {
			log.debug(" *********** Script " + this
					+ ", no more channels, stopping ...");
			scriptState = ScriptState.Stopped;
			onStop();
			Scripts.getInstance().remove(this);
		}
		return true;
	}

	public abstract String getDisplayStr();

	protected abstract void processEvent(Event event);

	protected abstract void onTimer();

	protected abstract void onStop();
	
	protected abstract void onStart(Object data);

}
