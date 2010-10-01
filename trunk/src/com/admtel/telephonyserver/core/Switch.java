package com.admtel.telephonyserver.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.addresstranslators.DefaultASTAddressTranslator;
import com.admtel.telephonyserver.config.SwitchDefinition;
import com.admtel.telephonyserver.interfaces.AddressTranslator;
import com.admtel.telephonyserver.requests.ChannelRequest;
import com.admtel.telephonyserver.requests.Request;
import com.admtel.telephonyserver.requests.SwitchRequest;

public abstract class Switch {

	private static Logger log = Logger.getLogger(Switch.class);

	public enum SwitchStatus {
		NotReady, Ready
	};

	private SwitchDefinition definition;
	private SwitchStatus status;
	private AddressTranslator addressTranslator;

	private Map<String, Channel> channels = new HashMap<String, Channel>();
	private Map<String, Channel> synchronizedChannels = Collections
			.synchronizedMap(channels);
	protected MessageHandler messageHandler = 
		new QueuedMessageHandler(){

			@Override
			public void onMessage(Object message) {
				if (message instanceof BasicIoMessage){
					Switch.this.processBasicIoMessage((BasicIoMessage)message);
				}
				else
				if (message instanceof Request){
					Switch.this.processRequest((Request)message);
				}
				
			}
		
		};
	
	public Switch(SwitchDefinition definition) {
		this.definition = definition;
		setStatus(SwitchStatus.NotReady);
		
	}

	public SwitchDefinition getDefinition() {
		return definition;
	}
	public String getSwitchId() {
		return definition.getId();
	}

	public void setSwitchId(String switchId){
		
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

	public void start(){
		//TODO reload addressTranslator to pick up modifications
		setAddressTranslator(SmartClassLoader
				.createInstance(AddressTranslator.class, definition
						.getAddressTranslatorClass()));
		if (getAddressTranslator() == null) {
			setAddressTranslator(new DefaultASTAddressTranslator());
		}

	}

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

	public void setStatus(SwitchStatus status) {
		this.status = status;
	}

	public void setAddressTranslator(AddressTranslator addressTranslator) {
		this.addressTranslator = addressTranslator;
	}

	public AddressTranslator getAddressTranslator() {
		return addressTranslator;
	}

	final public void processRequest(Request request)
	{
		//TODO process switch requests (like reload switch, stop switch ...)
		if (request instanceof ChannelRequest){
			ChannelRequest channelRequest = (ChannelRequest) request;
			Channel channel = this.getChannel(channelRequest.getChannelId());
			channel.putMessage(request);
		}
	}
	abstract public void processBasicIoMessage(BasicIoMessage message);
}
