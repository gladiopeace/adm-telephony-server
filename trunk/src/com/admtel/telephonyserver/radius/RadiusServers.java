package com.admtel.telephonyserver.radius;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.RadiusDefinition;
import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.EventsManager;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;
import com.admtel.telephonyserver.events.OutboundAlertingEvent;
import com.admtel.telephonyserver.interfaces.Authorizer;
import com.admtel.telephonyserver.interfaces.EventListener;

public class RadiusServers implements DefinitionChangeListener, Authorizer,
		EventListener {

	//TODO, check when radius services are enabled. Throw an exception for scripts that try to use radius services when they're not available
	
	static Logger log = Logger.getLogger(RadiusServers.class);

	Map<String, RadiusServer> idMap = new HashMap<String, RadiusServer>();
	Random rnd = new Random(System.currentTimeMillis());

	private RadiusServers() {
		EventsManager.getInstance().addEventListener(this.getClass().toString(), this);
	}

	private boolean isEnabled(){
		return idMap.size()>0;
	}
	private void put(RadiusServer radiusServer) {
		if (radiusServer != null) {
			synchronized (this) {
				idMap.put(radiusServer.definition.getId(), radiusServer);
			}
		}
	}

	public Collection<RadiusServer> getAll() {
		return idMap.values();
	}

	public RadiusServer getById(String id) {
		if (id == null)
			return null;
		synchronized (this) {
			return idMap.get(id);
		}
	}

	private static class SingletonHolder {
		private static RadiusServers instance = new RadiusServers();
	}

	private RadiusServer getAvailableServer() {
		// TODO, round robin implementation, or least used, or failover
		Collection<RadiusServer> radiusServers = getAll();
		int index = rnd.nextInt(radiusServers.size());

		for (RadiusServer radiusServer : radiusServers) {
			if (index == 0) {
				return radiusServer;
			}
			index--;
		}
		return null;
	}

	public static RadiusServers getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof RadiusDefinition) {
			RadiusDefinition radiusDefinition = (RadiusDefinition) definition;
			RadiusServer radiusServer = new RadiusServer(radiusDefinition);
			put(radiusServer);
		}

	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public AuthorizeResult authorize(Channel channel, String username,
			String password, String address,
			String calledStationId, boolean routing, boolean number) {

		if (channel == null) {
			log.warn("authorize channel is null");
			return new AuthorizeResult();
		}
		if (idMap.isEmpty()){
			return new AuthorizeResult();
		}
		AuthorizeResult authorizeResult = getAvailableServer().authorize(
				channel, username, password, address,
				calledStationId, routing, number);
		if (authorizeResult.getAuthorized()) {
			channel.setUserName(authorizeResult.getUserName());
			// TODO set hangup time
		}

		return new AuthorizeResult();
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()) {
		case InboundAlerting:
			onIncomingEvent((InboundAlertingEvent) event);
			break;
		case Hangup:
			onHangupEvent((HangupEvent) event);
			break;
		case OutboundAlerting:
			onOutboundAlertingEvent((OutboundAlertingEvent) event);
			break;
		}
		return false;
	}

	private void onOutboundAlertingEvent(OutboundAlertingEvent event) {
		accountingStart(event.getChannel());
	}

	private void onIncomingEvent(InboundAlertingEvent event) {
		accountingStart(event.getChannel());
	}

	private void onHangupEvent(HangupEvent event) {
		accountingStop(event.getChannel());
	}

	@Override
	public boolean accountingInterimUpdate(Channel channel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accountingStart(Channel channel) {
		if (!isEnabled()) return false;
		RadiusServer radiusServer = getAvailableServer();
		if (radiusServer != null){
			radiusServer.accountingStart(channel);
		}
		return true;
	}

	@Override
	public boolean accountingStop(Channel channel) {
		if (!isEnabled()) return false;
		RadiusServer radiusServer = getAvailableServer();
		if (radiusServer != null){
			radiusServer.accountingStop(channel);
		}
		return true;
	}

}
