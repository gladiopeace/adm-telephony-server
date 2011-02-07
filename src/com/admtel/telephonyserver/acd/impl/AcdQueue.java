package com.admtel.telephonyserver.acd.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.admtel.telephonyserver.acd.impl.AcdAgent.Status;

public class AcdQueue {
	String name;
	int priority=0;
	Map<String, AcdAgent> agents = new HashMap<String, AcdAgent>();
	Queue<AcdChannel> waitingChannels = new PriorityQueue<AcdChannel>();
	public long timeout = 10000;

	public AcdQueue(){
		
	}
	public AcdQueue(String name) {
		this.name = name;
	}
	public AcdAgent getFreeAgent() {
		for (AcdAgent agent: agents.values()){
			if (agent.getStatus() == Status.Ready){
				return agent;
			}
		}
		return null;
	}
}
