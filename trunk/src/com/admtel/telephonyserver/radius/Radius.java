package com.admtel.telephonyserver.radius;

public final class Radius {
	static public AuthorizeResult authorize(String username, String password,
			String address, String serviceType, String calledStationId,
			String callingStationId, String loginIp, String serviceNumber,
			boolean routing, boolean number) {
		return RadiusServers.getInstance().authorize(username, password,
				address, serviceType, calledStationId, calledStationId,
				loginIp, serviceNumber, routing, number);
	}
}
