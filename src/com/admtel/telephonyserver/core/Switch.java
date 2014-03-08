package com.admtel.telephonyserver.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.addresstranslators.DefaultASTAddressTranslator;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.SwitchDefinition;
import com.admtel.telephonyserver.events.DisconnectCode;
import com.admtel.telephonyserver.interfaces.AddressTranslator;
import com.admtel.telephonyserver.misc.VariableMap;

public abstract class Switch {

	private static Logger log = Logger.getLogger(Switch.class);

	public enum SwitchStatus {
		NotReady, Stopped, Started, Starting, Stopping
	};

	private SwitchDefinition definition;
	private SwitchStatus status;
	private AddressTranslator addressTranslator;
	private boolean restart = false;

	private Map<String, Channel> channels = new HashMap<String, Channel>();
	private Map<String, Channel> synchronizedChannels = Collections
			.synchronizedMap(channels);

	protected MessageHandler messageHandler = new QueuedMessageHandler() {

		@Override
		public void onMessage(Object message) {			
			if (message instanceof BasicIoMessage) {
				Switch.this.processBasicIoMessage((BasicIoMessage) message);
			}
		}
	};

	public Switch(SwitchDefinition definition) {
		this.definition = definition;
		setStatus(SwitchStatus.NotReady);
		setAddressTranslator(SmartClassLoader
				.createInstance(AddressTranslator.class,
						definition.getAddressTranslatorClass()));
		if (getAddressTranslator() == null) {
			setAddressTranslator(new DefaultASTAddressTranslator());
		}
	}

	public SwitchDefinition getDefinition() {
		return definition;
	}

	public String getParameter(String key){
		return definition.getParameters().get(key);
	}
	public String getSwitchId() {
		return definition.getId();
	}

	public void setSwitchId(String switchId) {

	}

	public void addChannel(Channel channel) {
		if (channel != null) {
			synchronizedChannels.put(channel.getId(), channel);
			log.debug(String.format("Switch (%s) : Added channel %s",
					getDefinition().getId(), channel));
		}
	}

	public void removeChannel(Channel channel) {
		if (channel == null)
			return;
		synchronizedChannels.remove(channel.getId());
		if (channels.size() == 0 && status == SwitchStatus.Stopping) {
			status = SwitchStatus.Stopped;
			if (restart) {
				start();
			}
		}
		log.debug(String.format("Switch (%s) : Removed channel %s",
				getDefinition().getId(), channel.getId()));
	}

	public Channel getChannel(String channelId) {
		return synchronizedChannels.get(channelId);
	}

	public Collection<Channel> getAllChannels() {
		return channels.values();
	}

	public void start() {
		restart = false;
		status = SwitchStatus.Started;
	}

	public void stop(boolean forceStop) {		
		status = SwitchStatus.Stopping;
		if (forceStop) {
			this.hangupAllChannels();
		}
	}

	public void restart(boolean forceStop) {
		restart = true;
		stop(forceStop);
	}
	public SwitchStatus getStatus() {
		return status;
	}

	abstract public Result originate(String destination, long timeout,
			String callerId, String calledId, String script, VariableMap data);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((definition == null) ? 0 : definition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Switch)) {
			return false;
		}
		Switch other = (Switch) obj;
		if (definition == null) {
			if (other.definition != null) {
				return false;
			}
		} else if (!definition.equals(other.definition)) {
			return false;
		}
		return true;
	}

	public void setStatus(SwitchStatus status) {
		this.status = status;
	}

	synchronized public void setAddressTranslator(
			AddressTranslator addressTranslator) {
		this.addressTranslator = addressTranslator;
	}

	synchronized public AddressTranslator getAddressTranslator() {
		return addressTranslator;
	}

	abstract public void processBasicIoMessage(BasicIoMessage message);

	public boolean isAcceptingCalls() {
		return definition.isEnabled() && status == SwitchStatus.Started;
	}

	public void setDefinition(SwitchDefinition def) {
		if (!def.getAddressTranslatorClass().equals(
				definition.getAddressTranslatorClass())) {

			setAddressTranslator(SmartClassLoader.createInstance(
					AddressTranslator.class, def.getAddressTranslatorClass()));
			if (getAddressTranslator() == null) {
				setAddressTranslator(new DefaultASTAddressTranslator());
			}
		}
		definition = def;

	}
	public String toReadableString(){
		return String.format("%s|%s:%d|%s|%d\n", definition.getName(), definition.getAddress(), definition.getPort(), getStatus(), channels.size());
	}
	public String getName(){
		return definition.getName();		
	}
	public String getAddress(){
		return definition.getAddress();
	}
	public int getNumberOfChannels(){
		return channels.size();
	}
	public String getId(){
		return definition.getId();
	}
	public void hangupAllChannels(){
		List<Channel> chs = new ArrayList<Channel>(channels.values());
		for (Channel c:chs){
			c.hangup(DisconnectCode.Normal);
		}
	}
}
