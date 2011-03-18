package com.admtel.telephonyserver.acd;

public enum AgentDequeuePolicy {
	RoundRobin (0), Random(1), LeastUsed(2), LastUsed(3);
	
	private final int code;
	AgentDequeuePolicy (int code){
		this.code = code;
	}
	public int toInteger(){
		return code;
	}
}
