package com.admtel.telephonyserver.acd.impl;

import java.util.Map;

import com.admtel.telephonyserver.acd.AcdAgent;
import com.admtel.telephonyserver.acd.AcdQueue;

public interface AcdDataProvider {	
	public Map<String, AcdQueue> getQueues();
	public Map<String, AcdAgent> getAgents();
}
