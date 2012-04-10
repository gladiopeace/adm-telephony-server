package com.admtel.telephonyserver.core;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.RegistrarDefinition;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.RegisteredEvent;
import com.admtel.telephonyserver.events.UnregisteredEvent;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.RegistrarInterface;
import com.admtel.telephonyserver.registrar.UserLocation;

public class Registrar implements DefinitionChangeListener, RegistrarInterface,
		EventListener {

	Logger log = Logger.getLogger(Registrar.class);

	RegistrarInterface registrar;
	Boolean enabled;

	private Registrar() {

	}

	private static class SingletonHolder {
		private final static Registrar instance = new Registrar();
	}

	static public Registrar getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof RegistrarDefinition) {
			RegistrarDefinition registrarDefinition = (RegistrarDefinition) definition;
			registrar = SmartClassLoader.createInstance(
					RegistrarInterface.class,
					registrarDefinition.getClassName());
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
	public UserLocation find(String user) {
		if (registrar != null) {
			return registrar.find(user);
		}
		return null;
	}

	@Override
	public void register(UserLocation userLocation) {
		if (registrar != null) {
			log.trace(String.format("User (%s) registered", userLocation.getUsername()));
			registrar.register(userLocation);
		}
	}

	@Override
	public void unregister(String registrationId) {
		if (registrar != null) {
			log.trace(String.format("User (%s) unregistered", registrationId));
			registrar.unregister(registrationId);
		}
	}

	@Override
	public Collection<UserLocation> get(long start, long limit) {
		return registrar.get(start, limit);
	}

	@Override
	public long getCount() {
		return registrar.getCount();
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()) {
		case Registered:
		{
			RegisteredEvent re = (RegisteredEvent) event;
			register(new UserLocation( re.getSwitchId(), re.getSigProtocol(), re.getUser()));
			
		}
			break;
		case Unregistered:
		{
			UnregisteredEvent ue = (UnregisteredEvent) event;
			unregister(ue.getUser());
		}
			break;
		}
		return false;
	}
}
