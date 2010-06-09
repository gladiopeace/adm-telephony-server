package com.admtel.telephonyserver.radius;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.RadiusDefinition;
import com.admtel.telephonyserver.interfaces.Authorizer;

public class RadiusServers implements DefinitionChangeListener, Authorizer {

	Map<String, RadiusServer> idMap = new HashMap<String, RadiusServer>();
	Random rnd = new Random(System.currentTimeMillis());
	private RadiusServers(){
		
	}
	
	private void put(RadiusServer radiusServer){
		if (radiusServer != null){
			synchronized(this){				
				idMap.put(radiusServer.definition.getId(), radiusServer);
			}
		}
	}
	public Collection<RadiusServer>getAll(){
		return idMap.values();
	}
	public RadiusServer getById(String id){
		if (id == null) return null;
		synchronized(this){
			return idMap.get(id);
		}
	}
	private static class SingletonHolder {
		private static RadiusServers instance = new RadiusServers();
	}
	public static RadiusServers getInstance(){
		return SingletonHolder.instance;
	}
	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof RadiusDefinition){
			RadiusDefinition radiusDefinition = (RadiusDefinition) definition;
			RadiusServer radiusServer = new RadiusServer (radiusDefinition);
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
	public AuthorizeResult authorize(String username,
			String password, String address, String callingStationId,
			String calledStationId, boolean routing, boolean number) {
		//TODO, round robin implementation, or least used, or failover
		Collection<RadiusServer>radiusServers = getAll();
		int index = rnd.nextInt(radiusServers.size());
		
		for (RadiusServer radiusServer:radiusServers){
			if (index == 0){
				return radiusServer.authorize(username, password, address, callingStationId, calledStationId, routing, number);
			}
			index --;
		}
		return new AuthorizeResult();
	}

}
