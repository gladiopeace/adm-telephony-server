package com.admtel.telephonyserver.acd.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.admtel.telephonyserver.acd.ACcdervice;
import com.admtel.telephonyserver.requests.DialRequest;

public class AcdServiceImpl implements ACcdervice{
	Map<String, AcdQueue> acdQueues = new HashMap<String, AcdQueue>();
	Map<String, ACDChannel> channels = new HashMap<String, ACDChannel>();

	@Override
	public void removeChannel(String channelId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addChannel(String queueName, String channelId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<DialRequest> getNextDial() {
		// TODO Auto-generated method stub
		return null;
	}

}
