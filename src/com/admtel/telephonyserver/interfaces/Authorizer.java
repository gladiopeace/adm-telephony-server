package com.admtel.telephonyserver.interfaces;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.Conference;
import com.admtel.telephonyserver.core.Participant;
import com.admtel.telephonyserver.radius.AuthorizeResult;

public interface Authorizer {
	public AuthorizeResult authorize(Channel channel, String username,
			String password, String address, String serviceType,
			String calledStationId, boolean routing, boolean number);
	public boolean accountingStart(Channel channel);
	public boolean accountingInterimUpdate(Channel channel);
	public boolean accountingStop(Channel channel);
	public boolean accountingStart(Channel channel, Conference conference, Participant participant);
	public boolean accountingInterimUpdate(Channel channel, Conference conference, Participant participant);
	public boolean accountingStop(Channel channel, Conference conference, Participant participant);

}
