package com.admtel.telephonyserver.acd.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.admtel.telephonyserver.acd.impl.AcdAgent.Status;


public class AcdQueue {
	
	private String name;
	private int priority=0;
	
	private List<AcdAgent> agents = new ArrayList<AcdAgent>();
	private Queue<AcdChannel> waitingChannels = new PriorityQueue<AcdChannel>();
	private long timeout = 10000;

	private AgentDequeuePolicy agentDequeuePolicy = AgentDequeuePolicy.LastUsed; 
	private List<AcdAgent> rrAgents = new ArrayList<AcdAgent>();
 
	public AcdQueue(){
		
	}
	public AcdQueue(String name) {
		this.name = name;		
	}
	public AcdAgent getFreeAgent() {		
		List<AcdAgent> tAgents = agents;
		switch (agentDequeuePolicy){
		case LastUsed:
			Collections.sort(agents, AcdAgent.dateComparator);
			tAgents = agents;
			break;
		case Random:
			Collections.sort(agents, AcdAgent.randomComparator);
			tAgents = agents;
			break;
		case LeastUsed:
			Collections.sort(agents, AcdAgent.useComparator);
			tAgents = agents;
			break;
		case RoundRobin:
			Collections.rotate(rrAgents, 1);
			tAgents = rrAgents;
			break;
		}
		for (AcdAgent agent:tAgents){
			if (agent.getStatus() == Status.Ready){
				return agent;
			}
		}
		
		return null;
	}
	public void addAgent(AcdAgent acdAgent) {
		agents.add(acdAgent);			
		rrAgents.add(acdAgent);
	}
	public boolean hasWaitingChannels() {
		return waitingChannels.size() > 0;
	}
	public AcdChannel peek() {
		return waitingChannels.peek();
	}
	public AcdChannel poll() {
		return waitingChannels.poll();
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public void remove(AcdChannel channel) {
		waitingChannels.remove(channel);		
	}
	public void add(AcdChannel channel) {
		waitingChannels.add(channel);
	}
	public String getName() {
		return name;		
	}
	public Queue<AcdChannel> getChannels(){
		return waitingChannels;
	}
}
