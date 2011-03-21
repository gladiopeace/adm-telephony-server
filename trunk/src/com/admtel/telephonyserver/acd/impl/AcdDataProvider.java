package com.admtel.telephonyserver.acd.impl;

import java.util.List;
import java.util.Map;

import com.admtel.telephonyserver.acd.AcdAgent;
import com.admtel.telephonyserver.acd.AcdCall;
import com.admtel.telephonyserver.acd.AcdQueue;


public interface AcdDataProvider {	
	public Map<String, AcdQueue> getQueues();
	public Map<String, AcdAgent> getAgents();
	public List<AcdAgent> getAvailableQueueAgents(String queueId);
	public AcdQueue getQueueById(String queueId);
	public AcdQueue getQueueByName(String queueName);
	public void updateAgent(AcdAgent agent);
	public AcdAgent getAgentById(String agentId);
	public AcdAgent getAgentByName(String agentName);
	public void updateQueue(AcdQueue queue);
}
