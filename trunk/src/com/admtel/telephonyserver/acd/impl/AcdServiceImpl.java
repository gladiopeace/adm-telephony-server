package com.admtel.telephonyserver.acd.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
			if (channel.agent != null){
				channel.agent.setStatus(Status.Ready);
				channel.agent = null;
			}
			channel.acdQueue.waitingChannels.remove(channel);			
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
			acdQueue.waitingChannels.add(channel);
			
		}
		else{
			if (channel.agent == null){
				acdQueue.waitingChannels.add(channel);
			}
			else{
				channel.agent.setStatus(Status.Ready);
				channel.agent = null;
			}			
		}
		return true;
	}

	@Override
	synchronized public List<DialRequest> getNextDial() {
		List<DialRequest> requests = new ArrayList<DialRequest>();
		log.trace("AcdServiceImpl, getNextDial ...");
		
		for (AcdQueue acdQueue: acdQueues.values()){
			if (acdQueue.waitingChannels.size() > 0){
				AcdChannel channel = acdQueue.waitingChannels.peek();
				log.trace(String.format("Waiting channel(%s) - queue(%s) looking for a free agent", channel.channelId, channel.acdQueue.name));
				if (channel != null){
					AcdAgent agent = acdQueue.getFreeAgent();
					if (agent != null){
						channel = acdQueue.waitingChannels.poll();
						channel.agent = agent;
						agent.setStatus(Status.Busy);
						log.trace(String.format("Waiting channel(%s) - queue(%s) found agent (%s)", channel.channelId, channel.acdQueue.name, agent.name));
						requests.add(new DialRequest(channel.channelId, agent.address, acdQueue.timeout));
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
			if (channel.agent != null){
				channel.agent.setStatus(Status.Ready);
				channel.agent = null;
			}
			channel.acdQueue.waitingChannels.add(channel);			
			return true;
		}
		return false;
	}
}
