package com.admtel.telephonyserver.registrar;

import java.util.*;
import org.apache.log4j.Logger;

import com.admtel.telephonyserver.interfaces.RegistrarInterface;

public class SimpleRegistrar implements RegistrarInterface {

    static Logger log = Logger.getLogger(SimpleRegistrar.class);

    Map<String, UserLocation> database = new Hashtable<String, UserLocation>();

	@Override
	public UserLocation find(String user) {		
		return database.get(user);
	}

	@Override
	public void register(UserLocation userLocation) {
        log.trace("Registering : " + userLocation.getUsername());
		database.put(userLocation.username, userLocation);
	}

	@Override
	public void unregister(String username) {
		database.remove(username);
	}

	@Override
	public Collection<UserLocation> get(int start, int limit) {
		Collection<UserLocation> c = database.values();
        if (start > c.size()){
            return Collections.emptyList();
        }
        Iterator<UserLocation> it = c.iterator();
        for (int i=0;i<start;i++){
            it.next();
        }
        Collection<UserLocation> result = new ArrayList<UserLocation>(limit);
        while (it.hasNext()){
            result.add(it.next());
        }
		return result;
	}

    @Override
    public Collection<UserLocation> get() {
        return database.values();
    }

    @Override
	public long getCount() {
		return database.size();
	}

}
