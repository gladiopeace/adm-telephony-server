package com.admtel.telephonyserver.interfaces;

import com.admtel.telephonyserver.registrar.UserLocation;

public interface Registrar {
	public void register (UserLocation userLocation);
	public void unregister(String user);
	public UserLocation find(String user);
}
