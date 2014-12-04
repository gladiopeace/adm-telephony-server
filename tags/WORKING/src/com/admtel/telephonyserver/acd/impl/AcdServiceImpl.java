package com.admtel.telephonyserver.acd.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.acd.AcdCall;
import com.admtel.telephonyserver.acd.AcdService;
import com.admtel.telephonyserver.acd.AcdQueue;
import com.admtel.telephonyserver.acd.AcdAgent;

public class AcdServiceImpl implements AcdService {

	static Logger log = Logger.getLogger(AcdServiceImpl.class);

	public AcdDataProvider acdDataProvider;
	PriorityQueue<AcdCall> calls = new PriorityQueue<AcdCall>();
	Map<String, AcdCall> callsBM = new HashMap<String, AcdCall>();

	static private DateComparator dateComparator = new DateComparator();
	static private UseComparator useComparator = new UseComparator();
	static private RandomComparator randomComparator = new RandomComparator();

	static class DateComparator implements Comparator<AcdAgent> {

		@Override
		public int compare(AcdAgent arg0, AcdAgent arg1) {
			return arg0.getLastUsedDate().compareTo(arg1.getLastUsedDate());
		}

	}

	static class UseComparator implements Comparator<AcdAgent> {

		@Override
		public int compare(AcdAgent o1, AcdAgent o2) {
			return o1.getUseCounter().compareTo(o2.getUseCounter());
		}

	}

	static class RandomComparator implements Comparator<AcdAgent> {

		static Random rand = new Random(System.currentTimeMillis());

		@Override
		public int compare(AcdAgent o1, AcdAgent o2) {
			return rand.nextInt(3) - rand.nextInt(3);
		}

	}

	public void init() {
		log.trace("AcdServiceImpl initialized ... ");
	}

	@Override
	synchronized public boolean queueChannel(String queueName, String channelId,
			Date setupTime, int priority) {
		log.trace(String.format("Queueing channel (%s) in queue(%s)", queueName,
				channelId));
		AcdQueue queue = acdDataProvider.getQueueByName(queueName);
		if (queue != null) {
			AcdCall call = new AcdCall(channelId, priority, queue.getId(),
					queue.getPriority(), setupTime);
			queue.incrementCurrentSessions();
			acdDataProvider.updateQueue(queue);
			calls.add(call);
			callsBM.put(call.getChannelId(), call);
		} else {
			log.warn(String.format("Queue %s not found", queueName));
			return false;
		}
		return true;
	}

//	@Override
//	synchronized public List<DialRequest> getNextDial() {
//		List<DialRequest> requests = new ArrayList<DialRequest>();		
//
//		Iterator<AcdCall> it = calls.iterator();
//		while (it.hasNext()) {
//			AcdCall call = it.next();
//			List<AcdAgent> agents = acdDataProvider
//					.getAvailableQueueAgents(call.getQueueId());
//			log.trace(String.format("getNextDial returned %d agents", agents.size()));
//			if (!agents.isEmpty()) {
//				AcdQueue queue = acdDataProvider.getQueueById(call.getQueueId());
//				if (queue != null) {
//					switch (queue.getAgentDequeuePolicy()) {
//					case LastUsed:
//						Collections.sort(agents, dateComparator);
//
//						break;
//					case Random:
//						Collections.sort(agents, randomComparator);
//
//						break;
//					case LeastUsed:
//						Collections.sort(agents, useComparator);
//
//						break;
//					// TODO
//					/*
//					 * case RoundRobin: Collections.rotate(rrAgents, 1); break;
//					 */
//					}
//				}
//				if (callsBM.get(call.getChannelId()) == null) {
//					it.remove();
//				} else if (call.getAgentId() == null) {
//					AcdAgent agent = agents.get(0);
//					agent.setCallChannelId(call.getChannelId());
//					agent.setLastUsedDate(new Date());
//					call.setAgentId(agent.getId());
//					acdDataProvider.updateAgent(agent);
//					DialRequest dr =new DialRequest(call.getChannelId(), agent
//							.getAddress(), queue.getTimeout());
//					dr.setUserData("dialed_agent", agent.getId());
//					dr.setUserData("queue", call.getQueueId());
//					requests.add(dr);
//				}
//			}
//		}
//
//		return requests;
//	}

	@Override
	public void agentDialStarted(String agentId, String channelId) {
		log.trace(String.format("Agent(%s) dial started on channel (%s)", agentId, channelId));
			AcdAgent agent = acdDataProvider.getAgentById(agentId);
			if (agent != null) {
				agent.setChannelId(channelId);
				acdDataProvider.updateAgent(agent);
			}
	}
	@Override
	public void agentConnected(String agentId) {
		log.trace(String.format("*************Agent(%s) Connected .....", agentId));
		//Remove call from the queue
		AcdAgent agent = acdDataProvider.getAgentById(agentId);
		if (agent != null){
			log.trace(String.format("Removing channel (%s) from queue", agent.getCallChannelId()));
			AcdCall call = callsBM.get(agent.getCallChannelId());
			if (call != null){
				AcdQueue queue = acdDataProvider.getQueueById(call.getQueueId());
				queue.decrementCurrentSessions();
				acdDataProvider.updateQueue(queue);
			}
			callsBM.remove(agent.getCallChannelId());
		}
		
	}

	@Override
	public void callerDisconnected(String channelId) {
		AcdCall call = callsBM.get(channelId);
		if (call != null){
			AcdAgent agent = acdDataProvider.getAgentById(call.getAgentId());
			if (agent != null){
				agent.setCallChannelId(null);
				acdDataProvider.updateAgent(agent);
			}
			AcdQueue queue = acdDataProvider.getQueueById(call.getQueueId());
			if (queue != null){
				queue.decrementCurrentSessions();
				acdDataProvider.updateQueue(queue);
			}
			callsBM.remove(channelId);
			
		}		
	}

	@Override
	public void agentDisconnected(String agentId) {
		AcdAgent agent = acdDataProvider.getAgentById(agentId);
		if (agent != null){
			AcdCall call = callsBM.get(agent.getCallChannelId());
			if (call != null){
				call.setAgentId(null);
			}
			agent.setCallChannelId(null);
			agent.setChannelId(null);
			acdDataProvider.updateAgent(agent);
		}
		
	}
	@Override
	public AcdQueue[] getQueues() {
		Collection<AcdQueue> c = acdDataProvider.getQueues().values();
		return c.toArray(new AcdQueue[c.size()]);
	}

	@Override
	public AcdAgent[] getAgents() {
		Collection<AcdAgent> c = acdDataProvider.getAgents().values();
		return c.toArray(new AcdAgent[c.size()]);
	}

	@Override
	public AcdCall[] getQueueCalls(String queueId) {
		//Consider a map of maps for the binding map 
		log.trace("Get Queue Calls for queue " + queueId);
		List<AcdCall> c = new ArrayList<AcdCall>();
		Iterator<AcdCall> it = callsBM.values().iterator();
		while (it.hasNext()){
			AcdCall call = it.next();
			log.trace(String.format("getQueueCalls(%s), call = %s", queueId,
					call));
			if (queueId == null || queueId.isEmpty()
					|| call.getQueueId() == queueId) {
				c.add(call);
			}
		}
		log.trace(String
				.format("Get Queue Calls for queue %s, has %d calls, total queue has %d calls",
						queueId, c.size(), calls.size()));
		return c.toArray(new AcdCall[c.size()]);
	}
	

	@Override
	public void agentDialFailed(String agentId) {
		AcdAgent agent = acdDataProvider.getAgentById(agentId);
		if (agent != null){
			AcdCall call = callsBM.get(agent.getCallChannelId());
			if (call != null){
				call.setAgentId(null);
			}
			agent.setCallChannelId(null);
			agent.setChannelId(null);
			acdDataProvider.updateAgent(agent);
		}
		
	}

	@Override
	public AcdAgent getAgentById(String agentId) {
		return acdDataProvider.getAgentById(agentId);
	}

	@Override
	public AcdAgent getAgentByName(String agentName) {
		return acdDataProvider.getAgentByName(agentName);
	}

}
