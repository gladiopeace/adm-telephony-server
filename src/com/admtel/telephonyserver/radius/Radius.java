package com.admtel.telephonyserver.radius;


public final class Radius {
 static public AuthorizeResult authorize(String username,
			String password, String address, String callingStationId,
			String calledStationId, boolean routing, boolean number){
	 return RadiusServers.getInstance().authorize(username, password, address, callingStationId, calledStationId, routing, number);
 }
}
