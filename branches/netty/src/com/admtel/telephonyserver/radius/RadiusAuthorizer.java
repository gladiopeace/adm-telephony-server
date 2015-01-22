package com.admtel.telephonyserver.radius;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.interfaces.Authorizer;

public class RadiusAuthorizer implements Authorizer {

	static Logger log = Logger.getLogger(RadiusAuthorizer.class);

	@Override
	public AuthorizeResult authorize(String username, String password,
			String address, String serviceType, String calledStationId,
			String callingStationId, String loginIp, String serviceNumber,
			boolean routing, boolean number) {
		log.trace(String
				.format("authorizing : %s, %s, %s, %s, %s, %s, %s, %s, routing(%s), number(%s)",
						username, password, address, serviceType,
						calledStationId, callingStationId, loginIp,
						serviceNumber, routing, number));
		return Radius.authorize(username, password, address, serviceType,
				calledStationId, callingStationId, loginIp, serviceNumber,
				routing, number);
	}

}
