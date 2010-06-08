package com.admtel.telephonyserver.registrar;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.admtel.telephonyserver.interfaces.RegistrarInterface;

public class SimpleRegistrar implements RegistrarInterface {
	Map<String, UserLocation> database = new Hashtable<String, UserLocation>();
	@Override
	public UserLocation find(String user) {		
		return database.get(user);
	}

	@Override
	public void register(UserLocation userLocation) {		
		database.put(userLocation.user, userLocation);
	}

	@Override
	public void unregister(String user) {
		database.remove(user);
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
