package com.admtel.telephonyserver.acd.impl;

import java.util.Date;

public class ACDChannel implements Comparable<ACDChannel> {
	String channelId;
	Integer priority;
	AcdQueue queue;
	Date setupTime;

	public ACDChannel(String channelId, Date setupTime, Integer priority) {
		this.channelId = channelId;
		this.priority = priority;
		this.setupTime = setupTime;
	}

	@Override
	public int compareTo(ACDChannel acdChannel) {		
		int c1 = priority.compareTo(acdChannel.priority);
		if (c1 != 0) {
			return c1;
		}
		c1 = setupTime
				.compareTo(acdChannel.setupTime);
		return c1;
	}
}
