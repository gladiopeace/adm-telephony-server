package com.admtel.telephonyserver.registrar;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.admtel.telephonyserver.interfaces.RegistrarInterface;

public class SimpleRegistrar implements RegistrarInterface {
	Map<String, UserLocation> database = new Hashtable<String, UserLocation>();
	Map<String, String> registrationIndex = new Hashtable<String, String>();
	
	@Override
	public UserLocation find(String user) {		
		return database.get(user);
	}

	@Override
	public void register(UserLocation userLocation) {		
		database.put(userLocation.username, userLocation);
		registrationIndex.put(userLocation.registrationId, userLocation.username);
	}

	@Override
	public void unregister(String registrationId) {
		String username = registrationIndex.get(registrationId);
		if (username != null){
			database.remove(username);
			registrationIndex.remove(registrationId);
		}
	}

	@Override
	public Collection<UserLocation> get(long start, long limit) {
		return database.values();
	}

	@Override
	public long getCount() {
		return database.size();
	}

}
