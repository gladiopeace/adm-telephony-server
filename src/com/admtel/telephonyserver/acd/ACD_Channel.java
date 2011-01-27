package com.admtel.telephonyserver.acd;

import com.admtel.telephonyserver.core.Channel;

public class ACD_Channel implements Comparable<ACD_Channel> {
	Channel channel;
	Integer priority;

	public ACD_Channel(Channel channel, Integer priority) {
		this.channel = channel;
		this.priority = priority;
	}

	@Override
	public int compareTo(ACD_Channel arg0) {
		ACD_Channel acdChannel = (ACD_Channel) arg0;
		int c1 = priority.compareTo(acdChannel.priority);
		if (c1 != 0) {
			return c1;
		}
		c1 = channel.getSetupTime()
				.compareTo(acdChannel.channel.getSetupTime());
		return c1;
	}
}
