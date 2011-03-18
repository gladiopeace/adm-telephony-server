package com.admtel.telephonyserver.acd.data;

import java.util.List;
import java.util.Map;

import com.admtel.telephonyserver.acd.AcdAgent;
import com.admtel.telephonyserver.acd.AcdCall;
import com.admtel.telephonyserver.acd.AcdQueue;


public interface AcdDataProvider {	
	public Map<String, AcdQueue> getQueues();
	public Map<String, AcdAgent> getAgents();
	public List<AcdAgent> getAvailableQueueAgents(String queueId);
	public AcdQueue getQueue(String queueId);
	public void updateAgent(AcdAgent agent);
	public AcdAgent getAgent(String agentId);
	public void updateQueue(AcdQueue queue);
}
