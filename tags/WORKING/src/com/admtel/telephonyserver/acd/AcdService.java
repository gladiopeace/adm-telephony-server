package com.admtel.telephonyserver.acd;

import java.util.Date;
import java.util.Map;
import java.util.Queue;

import java.util.List;

import com.admtel.telephonyserver.acd.AcdAgent;
import com.admtel.telephonyserver.acd.AcdQueue;

public interface AcdService {
	public boolean queueChannel(String queueName, String channelId, Date setupDate, int priority);	
	
	//public List<DialRequest> getNextDial();

	public void callerDisconnected(String channelId);
	public void agentDialStarted(String agentId, String channelId);
	public void agentConnected(String agentId);
	public void agentDisconnected(String agentId);
	public AcdQueue[] getQueues();
	public AcdAgent[] getAgents();
	public AcdCall[] getQueueCalls(String queueId);
	public void agentDialFailed(String agentId);

	public AcdAgent getAgentById(String agentId);
	public AcdAgent getAgentByName(String agentName);
}
