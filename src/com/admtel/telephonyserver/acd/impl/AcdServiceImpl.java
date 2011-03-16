package com.admtel.telephonyserver.acd.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.acd.AcdService;
import com.admtel.telephonyserver.acd.impl.AcdAgent.Status;
import com.admtel.telephonyserver.requests.DialRequest;

public class AcdServiceImpl implements AcdService {

	static Logger log = Logger.getLogger(AcdServiceImpl.class);
	
	Map<String, AcdQueue> acdQueues;
	Map<String, AcdAgent> acdAgents;
	Map<String, AcdChannel> channels = new HashMap<String, AcdChannel>();
	
	public AcdDataProvider acdDataProvider;

	public void init(){
		acdQueues = acdDataProvider.getQueues();
		acdAgents = acdDataProvider.getAgents();
		
		log.trace("AcdServiceImpl initialized ... ");
		log.trace(String.format("acdQueues = %d, acdAgents = %d", acdQueues.size(), acdAgents.size()));
	}
	
	@Override
	synchronized public void unqueueChannel(String channelId) {
		log.trace(String.format("Unqueueing channel %s", channelId));
		AcdChannel channel = channels.get(channelId);
		if (channel != null){
			channels.remove(channelId);
			if (channel.getAgent() != null){
				channel.getAgent().setStatus(Status.Ready);
				channel.getAgent().setChannel(null);
				channel.setAgent(null);
			}
			channel.getAcdQueue().remove(channel);			
		}
	}

	@Override
	synchronized public boolean queueChannel(String queueName, String channelId, Date setupTime, int priority) {
		log.trace(String.format("Queueing channel (%s) in queue(%s)", queueName, channelId));
		AcdQueue acdQueue = acdQueues.get(queueName);
		if (acdQueue == null){
			log.warn(String.format("Queue %s doesn't exist", queueName));
			return false;
		}
		AcdChannel channel = channels.get(channelId);
		if (channel == null){
			channel = new AcdChannel (acdQueue, channelId, setupTime, priority);
			channels.put(channelId, channel);
			acdQueue.add(channel);
			
		}
		else{
			if (channel.getAgent() == null){
				acdQueue.add(channel);
			}
			else{
				channel.getAgent().setStatus(Status.Ready);
				channel.setAgent(null);
			}			
		}
		return true;
	}

	@Override
	synchronized public List<DialRequest> getNextDial() {
		List<DialRequest> requests = new ArrayList<DialRequest>();
		log.trace("AcdServiceImpl, getNextDial ...");
		
		for (AcdQueue acdQueue: acdQueues.values()){
			if (acdQueue.hasWaitingChannels()){
				AcdChannel channel = acdQueue.peek();
				log.trace(String.format("Waiting channel(%s) - queue(%s) looking for a free agent", channel.getChannelId(), channel.getAcdQueue()));
				if (channel != null){
					AcdAgent agent = acdQueue.getFreeAgent();
					if (agent != null){
						channel = acdQueue.poll();
						channel.setAgent(agent);
						agent.setChannel(channel);
						agent.setStatus(Status.Busy);
						log.trace(String.format("Waiting channel(%s) - queue(%s) found agent (%s)", channel.getChannelId(), channel.getAcdQueue().getName(), agent.getName()));
						requests.add(new DialRequest(channel.getChannelId(), agent.getAddress(), acdQueue.getTimeout()));
					}
				}
			}
		}
		return requests;
	}

	@Override
	public boolean containsChannel(String uniqueId) {		
		return channels.containsKey(uniqueId);
	}

	@Override
	synchronized public boolean requeueChannel(String channelId) {
		log.trace(String.format("Requeuing channel (%s)", channelId));
		AcdChannel channel = channels.get(channelId);
		if (channel != null){
			if (channel.getAgent() != null){
				channel.getAgent().setStatus(Status.Ready);
				channel.getAgent().setChannel(null);
				channel.setAgent(null);
			}
			channel.getAcdQueue().add(channel);			
			return true;
		}
		return false;
	}

	@Override
	public Map<String, AcdQueue> getQueues() {
		return acdQueues;
	}

	@Override
	public Queue<AcdChannel> getQueuedChannels(String queueId) {
		AcdQueue queue = acdQueues.get(queueId);
		if (queue == null){
			return null;
		}
		return queue.getChannels();
	}

	@Override
	public Map<String, AcdAgent> getAgents() {		
		return acdAgents;		
	}

	@Override
	public AcdAgent getAgentForChannel(String channelId) {
		AcdChannel channel = channels.get(channelId);
		if (channel != null){
			return channel.getAgent();
		}
		return null;
	}

	@Override
	public AcdChannel getChannelForAgent(String agentId) {
		AcdAgent agent = acdAgents.get(agentId);
		if (agent != null && agent.getChannel() != null){
			return agent.getChannel();
		}
		return null;
	}

	@Override
	public AcdAgent getAgent(String agentId) {
		return acdAgents.get(agentId);
	}
}
