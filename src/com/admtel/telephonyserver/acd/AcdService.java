package com.admtel.telephonyserver.acd;

import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import java.util.List;

import com.admtel.telephonyserver.acd.impl.AcdAgent;
import com.admtel.telephonyserver.acd.impl.AcdChannel;
import com.admtel.telephonyserver.acd.impl.AcdQueue;
import com.admtel.telephonyserver.requests.DialRequest;

public interface AcdService {
	public boolean queueChannel(String queueName, String channelId, Date setupDate, int priority);
	public void unqueueChannel(String channelId);
	public List<DialRequest> getNextDial();
	public boolean containsChannel(String uniqueId);
	public boolean requeueChannel(String channelId);
	public Map<String, AcdQueue> getQueues ();
	public Queue<AcdChannel> getQueuedChannels(String queueId);
	public Map<String, AcdAgent> getAgents();
	public AcdAgent getAgentForChannel(String channelId);
	public AcdChannel getChannelForAgent(String agentId);
	public AcdAgent getAgent(String agentId);
}
