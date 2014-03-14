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
import com.admtel.telephonyserver.core.Switch.SwitchStatus;
import com.admtel.telephonyserver.events.ChannelEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.DisconnectedEvent;
import com.admtel.telephonyserver.freeswitch.FSSwitch;
import com.admtel.telephonyserver.interfaces.EventListener;

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
	/////////////////////////////////////////////////////////////////////////////////////////
	public void remove(Switch _switch){
		if (_switch != null){
			synchronized (this){
				idMap.remove(_switch.getDefinition().getId());
				addressMap.remove(_switch.getDefinition().getAddress());
			}
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof SwitchDefinition) {
			SwitchDefinition switchDefinition = (SwitchDefinition) definition;

			log.debug(String.format("Loading switch %s", definition.getId()));

			switch (switchDefinition.getSwitchType()) {
			case Asterisk: {
				ASTSwitch _switch = new ASTSwitch(switchDefinition);
				put(_switch);
				if (_switch.getDefinition().isEnabled()) {
					_switch.start();
				}
			}
				break;
			case Freeswitch: {
				Switch _switch = new FSSwitch(switchDefinition);
				put(_switch);
				if (_switch.getDefinition().isEnabled()) {
					_switch.start();
				}
			}
				break;
			}
		}
	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		if (definition instanceof SwitchDefinition) {
			Switch _switch = this.getById(definition.getId());
			log.trace("Definition removed - " + definition);
			if (_switch != null){				
				_switch.scheduleRemove();
			}
		}
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		log.trace(String.format("Definition changed from {%s} to {%s}", oldDefinition, newDefinition));
		if (newDefinition.isCoreChange(oldDefinition)){
			definitionRemoved(oldDefinition);
			definitionAdded(newDefinition);
		}
		else{
			SwitchDefinition def = (SwitchDefinition) newDefinition;
			Switch _switch = this.getById(def.getId());
			if (_switch != null){
				_switch.setDefinition(def);
			}
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////
	private void addChannel(Channel channel) {
		if (channel != null) {
			synchronizedChannels.put(channel.getUniqueId(), channel);
			log.debug(String.format("Switches : Added channel %s", channel));
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	private void removeChannel(Channel channel) {
		if (channel == null)
			return;
		synchronizedChannels.remove(channel.getUniqueId());
		log.debug(String.format("Switches : Removed channel %s", channel));
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()) {
		case Disconnected: {
			DisconnectedEvent he = (DisconnectedEvent) event;
			removeChannel(he.getChannel());
		}
			break;
		case Alerting:
		case Offered:
			ChannelEvent ce = (ChannelEvent) event;
			addChannel(ce.getChannel());

			break;
		}

		return false;
	}

	public String toReadableString() {
		String result ="";
		for (Switch _switch:idMap.values()){
			result += _switch.toReadableString();
		}
		return result;
	}
	
	public void start() {
		for (Switch _switch:idMap.values()) {
			_switch.start();
		}
	}
	public void stop(boolean forceStop) {
		for (Switch _switch:idMap.values()) {
			_switch.stop(forceStop);
		}
	}
	public void restart(boolean forceStop) {
		for (Switch _switch:idMap.values()) {
			_switch.restart(forceStop);
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<Switch> getAll() {
		Collection<Switch> switches = new ArrayList<Switch>();
		for (Switch _switch:idMap.values()){
				switches.add(_switch);
		}
		return switches;
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	public List<Channel> getAllChannels() {
		List<Channel> result = new ArrayList<Channel>();
		result.addAll(channels.values());
		return result;
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	public List<Channel> getChannelsWithOffsetAndCount(int offset, int count){
		List<Channel> result = new ArrayList<Channel>(count);
		ArrayList<Channel>tChannels = new ArrayList<Channel>();
		tChannels.addAll(channels.values());
		int size = channels.size();
		
		for (int i=0;i<count;i++){
			if ((i+offset) >= size) break;
			result.add(tChannels.get(i+offset));
		}
		log.trace("GetChannelsWithOffsetAndCount : " + result.size());
		return result;
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	public int getChannelCount(){
		return channels.size();
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	public Channel getChannelById(String id){
		return channels.get(id);
	}

	/////////////////////////////////////////////////////////////////////////////////////////	
	public Switch getByAddress(String address) {
		if (address == null)
			return null;
		synchronized (this) {
			return addressMap.get(address);
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	public Switch getById(String id) {
		if (id == null)
			return null;
		synchronized (this) {
			return idMap.get(id);
		}
	}
	public Map<String, Channel>getChannels(){
		return channels;
	}
	/////////////////////////////////////////////////////////////////////////////////////////
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
	/////////////////////////////////////////////////////////////////////////////////////////	
	public Switch getLeastUsed() {
		Switch leastUsed = null;
		Collection<Switch> switches = getAll();
		int index = switches.size() -1;
		for (Switch _switch : switches) {
			if (index == switches.size() - 1){
				leastUsed = _switch;
			}else if (index < 0){
				return leastUsed;
			}else{
				if (leastUsed.getAllChannels().size() > _switch.getAllChannels().size()){
				leastUsed = _switch;	
				}					
			}
			index--;
		}
		return leastUsed;
	}
}
 