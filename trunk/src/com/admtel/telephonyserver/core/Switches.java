package com.admtel.telephonyserver.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.asterisk.ASTSwitch;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.SwitchDefinition;
import com.admtel.telephonyserver.events.ChannelEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.freeswitch.FSSwitch;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.requests.ChannelRequest;
import com.admtel.telephonyserver.requests.Request;
import com.admtel.telephonyserver.requests.SwitchRequest;

public class Switches implements DefinitionChangeListener, EventListener {

	private static Logger log = Logger.getLogger(Switches.class);

	Map<String, Switch> idMap = new HashMap<String, Switch>();
	Map<String, Switch> addressMap = new HashMap<String, Switch>();

	private Map<String, Channel> channels = new HashMap<String, Channel>();
	private Map<String, Channel> synchronizedChannels = Collections
			.synchronizedMap(channels);

	Random rnd = new Random(System.currentTimeMillis());

	private Switches() {

	}

	private static class SingletonHolder {
		private static Switches instance = new Switches();
	}

	public static Switches getInstance() {
		return SingletonHolder.instance;
	}

	// operation
	private void put(Switch _switch) {
		if (_switch != null) {
			synchronized (this) {
				idMap.put(_switch.getDefinition().getId(), _switch);
				addressMap.put(_switch.getDefinition().getAddress(), _switch);
			}
		}
	}

	public Collection<Switch> getAll() {
		return idMap.values();
	}

	public List<Channel> getAllChannels() {
		Collection<Switch> switches = idMap.values();
		List<Channel> result = new ArrayList<Channel>();
		for (Switch _switch : switches) {
			result.addAll(_switch.getAllChannels());
		}
		return result;
	}

	public Switch getByAddress(String address) {
		if (address == null)
			return null;
		synchronized (this) {
			return addressMap.get(address);
		}
	}

	public Switch getById(String id) {
		if (id == null)
			return null;
		synchronized (this) {
			return idMap.get(id);
		}
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof SwitchDefinition) {
			SwitchDefinition switchDefinition = (SwitchDefinition) definition;

			log.debug(String.format("Loading switch %s", definition.getId()));

			if (getById(definition.getId()) != null) {
				log.debug(String.format("Switch %s, already loaded", definition
						.getId()));
				return;
			}

			switch (switchDefinition.getSwitchType()) {
			case Asterisk: {
				ASTSwitch _switch = new ASTSwitch(switchDefinition);
				put(_switch);
				_switch.start();
			}
				break;
			case Freeswitch: {
				Switch _switch = new FSSwitch(switchDefinition);
				put(_switch);
				_switch.start();
			}
				break;
			}
		}
	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		if (definition instanceof SwitchDefinition) {
			// TODO
		}
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		if (newDefinition instanceof SwitchDefinition) {
			// TODO
		}

	}

	public Switch getRandom() {
		Collection<Switch> switches = getAll();
		int index = rnd.nextInt(switches.size());
		for (Switch _switch : switches) {
			if (index == 0)
				return _switch;
			index--;
		}
		return null;
	}

	public void processRequest(Request request) {
		if (request instanceof ChannelRequest) {
			ChannelRequest channelRequest = (ChannelRequest) request;
			Channel channel = synchronizedChannels.get(channelRequest
					.getChannelId());
			log.trace(String.format("Request(%s) for channel (%s)", request,
					channel));
			if (channel != null) {
				channel.putMessage(request);
			}
		} 
	}

	private void addChannel(Channel channel) {
		if (channel != null) {
			synchronizedChannels.put(channel.getUniqueId(), channel);
			log.debug(String.format("Switches : Added channel %s", channel));
		}
	}

	private void removeChannel(Channel channel) {
		if (channel == null)
			return;
		synchronizedChannels.remove(channel.getUniqueId());
		log.debug(String.format("Switches : Removed channel %s", channel));
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()) {
		case Hangup: {
			HangupEvent he = (HangupEvent) event;
			removeChannel(he.getChannel());
		}
			break;
		case InboundAlerting:
		case OutboundAlerting:
			ChannelEvent ce = (ChannelEvent) event;
			addChannel(ce.getChannel());

			break;
		}

		return false;
	}
}
