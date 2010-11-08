package com.admtel.telephonyserver.misc;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.admtel.telephonyserver.interfaces.Authorizer;
import com.admtel.telephonyserver.radius.AuthorizeResult;

public class SimpleAuthorizer implements Authorizer{

	@Override
	public AuthorizeResult authorize(String username, String password,
			String address, String serviceType, String calledStationId,
			String callingStationId, String loginIp, String serviceNumber,
			boolean routing, boolean number) {
		AuthorizeResult result = new AuthorizeResult();
		
		ArrayList<String> routes = new ArrayList<String>();
		routes.add("sip/1000");
		
		result.setUserName(username);
		result.setAuthorized(true);
		result.setAllowedTime(1000);
		result.setRoutes(routes);
		result.setCredit(new BigDecimal(10.5));
		return result;
	}

}
