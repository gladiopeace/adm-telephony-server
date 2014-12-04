package com.admtel.telephonyserver.core;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.Channel.CallState;
import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.DisconnectCode;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.DisconnectedEvent;
import com.admtel.telephonyserver.events.AlertingEvent;
import com.admtel.telephonyserver.interfaces.EventListener;

import com.admtel.telephonyserver.interfaces.Authorizer;
import com.admtel.telephonyserver.radius.AuthorizeResult;
import com.admtel.telephonyserver.registrar.UserLocation;

public abstract class Script implements EventListener {

	private static Logger gLog = Logger.getLogger(Script.class);

	enum ScriptState {
		Running, Stopped
	};

	String id;

	ScriptState scriptState = ScriptState.Running;

	Map<String, String> parameters;

	List<Channel> channels = new ArrayList<Channel>();

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String getParameter(String key) {
		if (parameters != null) {
			return parameters.get(key);
		}
		return null;
	}

	public String getParameter(String key, String defaultValue) {
		if (parameters == null || parameters.get(key) == null) {
			return defaultValue;
		}
		return parameters.get(key);
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public ScriptState getState() {
		return scriptState;
	}

	public Script() {
		id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public String dump() {
		return dump(0);
	}

	public String dump(int level) {
		String result = this.getClass().getSimpleName()
				+ String.format(":State(%s)-Channels(%d)", scriptState,
						channels.size());
		return result;
	}

	synchronized final public boolean onEvent(Event event) {

		try {
			gLog.trace(this + ", got event " + event);

			try {
				processEvent(event);
			} catch (Exception e) {
				gLog.warn(e.toString());
			}

			if (event.getEventType() == Event.EventType.Disconnected) {
				DisconnectedEvent he = (DisconnectedEvent) event;
				removeChannel(he.getChannel());
			}
			if (channels.size() == 0) {
				stop();
			}
		} catch (Exception e) {
			gLog.fatal(this + ", " + e.getMessage(), e);
		}
		return true;
	}

	private void stop() {
		onStop();
		ScriptManager.getInstance().deleteScript(this);
		scriptState = ScriptState.Stopped;
	}

	public abstract String getDisplayStr();

	protected abstract void processEvent(Event event);

	protected abstract void onTimer();

	protected abstract void onStop();

	public abstract void onStart();

	synchronized public void addChannel(Channel channel) {
		if (channel == null)
			return;
		if (channels.contains(channel))
			return;
		channels.add(channel);
		channel.addEventListener(this);
		channel.setScript(this);
	}

	public void removeChannel(Channel channel) {
		if (channel == null)
			return;
		channel.removeEventListener(this);
		channels.remove(channel);
	}

	public Channel getIncomingChannel() {
		return getChannel(CallOrigin.Inbound);
	}

	public Channel getChannel(CallOrigin callOrigin) {
		for (Channel c : channels) {
			if (c.getCallOrigin() == callOrigin) {
				return c;
			}
		}
		return null;

	}

	public Channel getChannel(CallOrigin callOrigin, CallState state) {
		for (Channel c : channels) {
			if (c.getCallOrigin() == callOrigin && c.getCallState() == state) {
				return c;
			}
		}

		return null;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void hangupAll() {
		for (Channel c : channels) {
			c.hangup(DisconnectCode.Normal);
		}
	}
}
