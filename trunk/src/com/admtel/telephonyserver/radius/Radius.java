package com.admtel.telephonyserver.radius;

import com.admtel.telephonyserver.core.Channel;


public final class Radius {
 static public AuthorizeResult authorize(Channel channel, String username,
			String password, String address, String serviceType, String callingStationId,
			String calledStationId, boolean routing, boolean number){
	 return RadiusServers.getInstance().authorize(channel, username, password, address, serviceType, calledStationId, routing, number);
 }
}
