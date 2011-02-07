package com.admtel.telephonyserver.acd.impl;

import java.util.HashMap;
import java.util.Map;


public class AcdDataProviderImpl implements AcdDataProvider {	
	
	Map<String, AcdQueue> queues = new HashMap<String, AcdQueue>();	
	Map<String, AcdAgent> agents = new HashMap<String, AcdAgent>();
	
	public AcdDataProviderImpl(){
		agents.put("agent_1", new AcdAgent("agent_1", "sip:agent_1"));
		agents.put("agent_2", new AcdAgent("agent_2", "sip:agent_2"));
		agents.put("agent_3", new AcdAgent("agent_3", "sip:agent_3"));
		agents.put("agent_4", new AcdAgent("agent_4", "sip:agent_4"));
		AcdQueue queue = new AcdQueue("queue_1");
		queues.put("queue_1", queue);
		queue.agents.put("agent_1", agents.get("agent_1"));
		queue.agents.put("agent_2", agents.get("agent_2"));
		queue.agents.put("agent_3", agents.get("agent_3"));
		
		queue = new AcdQueue("queue_2");
		queues.put("queue_2", queue);
		queue.agents.put("agent_3", agents.get("agent_3"));
		queue.agents.put("agent_4", agents.get("agent_4"));			
	}
	
	public void init(){
								
	}

	@Override
	public Map<String, AcdQueue> getQueues() {
		return queues;
	}

	@Override
	public Map<String, AcdAgent> getAgents() {
		return agents;
	}
	
	
}
