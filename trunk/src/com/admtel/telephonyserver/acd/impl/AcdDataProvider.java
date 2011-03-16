package com.admtel.telephonyserver.acd.impl;

import java.util.Map;


public interface AcdDataProvider {	
	public Map<String, AcdQueue> getQueues();
	public Map<String, AcdAgent> getAgents();
}
