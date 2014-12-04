package com.admtel.telephonyserver.interfaces;

import java.util.Collection;

import com.admtel.telephonyserver.registrar.UserLocation;

public interface RegistrarInterface {
	public void register (UserLocation userLocation);
	public void unregister(String username);
	public UserLocation find(String username);
	public Collection<UserLocation> get(int start, int limit);
    public Collection<UserLocation> get();
	public long getCount();
}
