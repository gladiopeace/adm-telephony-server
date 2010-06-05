package com.admtel.telephonyserver.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.addresstranslators.DefaultASTAddressTranslator;
import com.admtel.telephonyserver.config.SwitchDefinition;
import com.admtel.telephonyserver.interfaces.AddressTranslator;

public abstract class Switch {

	static Logger log = Logger.getLogger(Switch.class);

	public enum SwitchStatus {
		NotReady, Ready
	};

	SwitchDefinition definition;
	SwitchStatus status;
	AddressTranslator addressTranslator;

	private Map<String, Channel> channels = new HashMap<String, Channel>();
	private Map<String, Channel> synchronizedChannels = Collections
			.synchronizedMap(channels);

	public Switch(SwitchDefinition definition) {
		this.definition = definition;
		status = SwitchStatus.NotReady;
		addressTranslator = SmartClassLoader
				.createInstance(AddressTranslator.class, definition
						.getAddressTranslatorClass());
		if (addressTranslator == null) {
			addressTranslator = new DefaultASTAddressTranslator();
		}
	}

	public SwitchDefinition getDefinition() {
		return definition;
	}
	public String getSwitchId() {
		return definition.getId();
	}

	public void addChannel(Channel channel) {
		if (channel != null) {
			synchronizedChannels.put(channel.getId(), channel);
			log.debug(String.format("Switch (%s) : Added channel %s",
					getDefinition().getId(), channel.getId()));
		}
	}

	public void removeChannel(Channel channel) {
		if (channel == null)
			return;
		synchronizedChannels.remove(channel.getId());
		log.debug(String.format("Switch (%s) : Removed channel %s",
				getDefinition().getId(), channel.getId()));
	}

	public Channel getChannel(String channelId) {
		return synchronizedChannels.get(channelId);
	}

	public Collection<Channel> getAllChannels() {
		return channels.values();
	}

	abstract public void start();

	public SwitchStatus getStatus() {
		return status;
	}

	abstract public Result originate(String destination, long timeout,
			String callerId, String calledId, String script,
			String data);

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
	
}