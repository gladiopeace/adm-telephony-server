package com.admtel.telephonyserver.interfaces;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.radius.AuthorizeResult;

public interface Authorizer {
	public AuthorizeResult authorize(Channel channel, String username,
			String password, String address,
			String calledStationId, boolean routing, boolean number);
	public boolean accountingStart(Channel channel);
	public boolean accountingInterimUpdate(Channel channel);
	public boolean accountingStop(Channel channel);
}
