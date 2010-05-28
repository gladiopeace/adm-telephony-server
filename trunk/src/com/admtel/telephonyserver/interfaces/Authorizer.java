package com.admtel.telephonyserver.interfaces;

import com.admtel.telephonyserver.radius.AuthorizeResult;

public interface Authorizer {
	public AuthorizeResult authorize(String username,
			String password, String address, String callingStationId,
			String calledStationId, boolean routing, boolean number);
}
