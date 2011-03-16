package com.admtel.telephonyserver.acd.impl;

import java.util.Date;



public class AcdChannel implements Comparable<AcdChannel> {
	private String channelId;
	Integer priority;
	private AcdQueue acdQueue;
	Date setupTime;
	
	private AcdAgent agent;

	public AcdChannel(AcdQueue acdQueue, String channelId, Date setupTime, Integer priority) {
		this.setAcdQueue(acdQueue);
		this.setChannelId(channelId);
		this.priority = priority;
		this.setupTime = setupTime;
	}

	@Override
	public int compareTo(AcdChannel acdChannel) {		
		int c1 = priority.compareTo(acdChannel.priority);
		if (c1 != 0) {
			return c1;
		}
		c1 = setupTime.compareTo(acdChannel.setupTime);
		return c1;
	}

	public void setAgent(AcdAgent agent) {
		this.agent = agent;
	}

	public AcdAgent getAgent() {
		return agent;
	}

	public void setAcdQueue(AcdQueue acdQueue) {
		this.acdQueue = acdQueue;
	}

	public AcdQueue getAcdQueue() {
		return acdQueue;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelId() {
		return channelId;
	}
}