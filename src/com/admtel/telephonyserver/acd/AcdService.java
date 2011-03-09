package com.admtel.telephonyserver.acd;

import java.util.Date;
import java.util.Set;

import java.util.List;
import com.admtel.telephonyserver.requests.DialRequest;

public interface AcdService {
	public boolean queueChannel(String queueName, String channelId, Date setupDate, int priority);
	public void unqueueChannel(String channelId);
	public List<DialRequest> getNextDial();
	public boolean containsChannel(String uniqueId);
	public boolean requeueChannel(String channelId);
	public String[] getQueues ();
	public String[] getQueuedChannels(String queueId);
	public String[] getAgents();
	public String getAgentForChannel(String channelId);
}
