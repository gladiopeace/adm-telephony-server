package com.admtel.telephonyserver.interfaces;

import java.util.Collection;

import com.admtel.telephonyserver.registrar.UserLocation;

public interface RegistrarInterface {
	public void register (UserLocation userLocation);
	public void unregister(String user);
	public UserLocation find(String user);
	public Collection<UserLocation> get(long start, long limit);
	public long getCount();
}
