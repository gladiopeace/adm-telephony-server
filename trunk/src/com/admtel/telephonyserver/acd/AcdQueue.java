package com.admtel.telephonyserver.acd;

public class AcdQueue {
	
	String id;
	String name;
	Integer priority=0;
	Integer timeout=10000;
	Integer currentSessions=0;
	
	public void incrementCurrentSessions(){
		currentSessions ++;
	}
	public void decrementCurrentSessions(){
		currentSessions--;
	}
	public Integer getCurrentSessions() {
		return currentSessions;
	}
	public void setCurrentSessions(Integer currentSessions) {
		this.currentSessions = currentSessions;
	}
	private AgentDequeuePolicy agentDequeuePolicy = AgentDequeuePolicy.LastUsed; 
	
	public AcdQueue(String id, String name,  Integer priority, Integer timeout) {
		super();
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.timeout = timeout;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AgentDequeuePolicy getAgentDequeuePolicy() {
		return agentDequeuePolicy;
	}
	public void setAgentDequeuePolicy(AgentDequeuePolicy agentDequeuePolicy) {
		this.agentDequeuePolicy = agentDequeuePolicy;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
}
