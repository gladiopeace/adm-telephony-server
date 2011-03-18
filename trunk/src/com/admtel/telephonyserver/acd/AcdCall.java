package com.admtel.telephonyserver.acd;

import java.util.Date;

public class AcdCall implements Comparable<AcdCall> {
	
	String channelId;
	Integer priority;
	String queueId;
	Integer queuePriority;
	Date setupTime;
	String agentId;

	public AcdCall(String channelId, Integer priority, String queueId,
			Integer queuePriority, Date setupTime) {
		super();
		this.channelId = channelId;
		this.priority = priority;
		this.queueId = queueId;
		this.queuePriority = queuePriority;
		this.setupTime = setupTime;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getQueueId() {
		return queueId;
	}

	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}

	public Integer getQueuePriority() {
		return queuePriority;
	}

	public void setQueuePriority(Integer queuePriority) {
		this.queuePriority = queuePriority;
	}

	public Date getSetupTime() {
		return setupTime;
	}

	public void setSetupTime(Date setupTime) {
		this.setupTime = setupTime;
	}

	@Override
	public int compareTo(AcdCall acdChannel) {
		int c1 = queuePriority.compareTo(acdChannel.queuePriority);
		if (c1 != 0) {
			return c1;
		}
		c1 = priority.compareTo(acdChannel.priority);
		if (c1 != 0) {
			return c1;
		}
		c1 = setupTime.compareTo(acdChannel.setupTime);
		return c1;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AcdCall [");
		if (channelId != null) {
			builder.append("channelId=");
			builder.append(channelId);
			builder.append(", ");
		}
		if (priority != null) {
			builder.append("priority=");
			builder.append(priority);
			builder.append(", ");
		}
		if (queueId != null) {
			builder.append("queueId=");
			builder.append(queueId);
			builder.append(", ");
		}
		if (queuePriority != null) {
			builder.append("queuePriority=");
			builder.append(queuePriority);
			builder.append(", ");
		}
		if (setupTime != null) {
			builder.append("setupTime=");
			builder.append(setupTime);
			builder.append(", ");
		}
		if (agentId != null) {
			builder.append("agentId=");
			builder.append(agentId);
		}
		builder.append("]");
		return builder.toString();
	}

}