package com.admtel.telephonyserver.acd.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.admtel.telephonyserver.acd.AcdAgent;
import com.admtel.telephonyserver.acd.AcdAgentStatus;
import com.admtel.telephonyserver.acd.AcdCall;
import com.admtel.telephonyserver.acd.AcdQueue;

public class SimpleAcdDataProvider implements AcdDataProvider {

	Map<String, AcdQueue> queues = new HashMap<String, AcdQueue>();
	Map<String, AcdAgent> agents = new HashMap<String, AcdAgent>();
	
	Map<String, List<AcdAgent>> queueAgents = new HashMap<String, List<AcdAgent>>();
	
	public void init(){
		AcdQueue queue_1 = new AcdQueue ("queue_1", 0, 10000);
		AcdQueue queue_2 = new AcdQueue ("queue_2", 0, 10000);
		AcdAgent agent_1 = new AcdAgent ("agent_1", "agent_1", "user:agent_1");
		AcdAgent agent_2 = new AcdAgent ("agent_2", "agent_2", "user:agent_2");
		AcdAgent agent_3 = new AcdAgent ("agent_3", "agent_3", "user:agent_3");
		
		
		queues.put(queue_1.getId(), queue_1);
		queues.put(queue_2.getId(), queue_2);
		agents.put(agent_1.getId(), agent_1);
		agents.put(agent_2.getId(), agent_2);
		agents.put(agent_3.getId(), agent_3);
		
		List<AcdAgent> queue_1_list = new ArrayList<AcdAgent>();
		queue_1_list.add(agent_1);
		queue_1_list.add(agent_2);
		List<AcdAgent> queue_2_list = new ArrayList<AcdAgent>();
		queue_2_list.add(agent_2);
		queue_2_list.add(agent_3);
		queueAgents.put(queue_1.getId(), queue_1_list);
		queueAgents.put(queue_2.getId(), queue_2_list);
	}
	
	@Override
	public Map<String, AcdQueue> getQueues() {
		return queues;
	}

	@Override
	public Map<String, AcdAgent> getAgents() {
		return agents;
	}

	@Override
	public List<AcdAgent> getAvailableQueueAgents(String queueId) {
		List<AcdAgent> result = new ArrayList<AcdAgent>();
		List<AcdAgent> tAgents = queueAgents.get(queueId);
		if (queueAgents != null){
			for (AcdAgent agent:tAgents){
				if (agent.getChannelId() == null && agent.getStatus() == AcdAgentStatus.Ready){
					result.add(agent);
				}
			}
		}
		return result;
	}

	@Override
	public AcdQueue getQueue(String queueId) {
		return queues.get(queueId);
	}

	@Override
	public void updateAgent(AcdAgent agent) {		

	}

	@Override
	public AcdAgent getAgent(String agentId) {
		return agents.get(agentId);
	}

	@Override
	public void updateQueue(AcdQueue queue) {
		// TODO Auto-generated method stub
		
	}

}
