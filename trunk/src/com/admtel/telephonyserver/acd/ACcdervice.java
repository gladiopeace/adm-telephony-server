package com.admtel.telephonyserver.acd;

import java.util.List;

import com.admtel.telephonyserver.requests.DialRequest;

public interface ACcdervice {
	public void addChannel(String queueName, String channelId);
	public void removeChannel(String channelId);
	public List<DialRequest> getNextDial();
	
}
