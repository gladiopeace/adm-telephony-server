package com.admtel.telephonyserver.radius;

import com.admtel.telephonyserver.interfaces.Authorizer;

public class RadiusAuthorizer implements Authorizer {

	@Override
	public AuthorizeResult authorize(String username, String password,
			String address, String serviceType, String calledStationId,
			String callingStationId, String loginIp, String serviceNumber,
			boolean routing, boolean number) {
		return Radius.authorize(username, password, address, serviceType, calledStationId, callingStationId, loginIp, serviceNumber, routing, number);
	}

}
