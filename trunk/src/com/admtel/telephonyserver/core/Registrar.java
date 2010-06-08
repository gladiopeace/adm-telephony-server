package com.admtel.telephonyserver.core;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.RegistrarDefinition;
import com.admtel.telephonyserver.interfaces.RegistrarInterface;
import com.admtel.telephonyserver.registrar.UserLocation;

public class Registrar implements DefinitionChangeListener, RegistrarInterface {

	Logger log = Logger.getLogger(Registrar.class);
	
	RegistrarInterface registrar;
	Boolean enabled;
	
	private Registrar(){
		
	}
	private static class SingletonHolder {
		private final static Registrar instance = new Registrar();
	}
	
	static public Registrar getInstance(){
		return SingletonHolder.instance;
	}
	
	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof RegistrarDefinition){
			RegistrarDefinition registrarDefinition = (RegistrarDefinition)definition;
			registrar = SmartClassLoader.createInstance(RegistrarInterface.class, registrarDefinition.getClassName());
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
		if (registrar != null){
			return registrar.find(user);
		}
		return null;
	}

	@Override
	public void register(UserLocation userLocation) {
		if (registrar != null){
			registrar.register(userLocation);
		}
	}

	@Override
	public void unregister(String user) {
		if (registrar != null){
			registrar.unregister(user);
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

}
