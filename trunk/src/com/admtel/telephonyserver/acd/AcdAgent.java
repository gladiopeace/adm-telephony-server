package com.admtel.telephonyserver.acd;

import java.util.Date;

public class AcdAgent {
	
	String id;
	String name;
	String password;
	String address;
	AcdAgentStatus status = AcdAgentStatus.Ready;
	String channelId;	
	String callChannelId;
	Date lastUsedDate = new Date();
	Integer useCounter = 0;

	

	public AcdAgent(String id, String name, String password, String address) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.address = address;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setLastUsedDate(Date lastUsedDate) {
		this.lastUsedDate = lastUsedDate;
	}
	public Date getLastUsedDate() {
		return lastUsedDate;
	}
	public void setUseCounter(Integer useCounter) {
		this.useCounter = useCounter;
	}
	public Integer getUseCounter() {
		return useCounter;
	}
	public AcdAgentStatus getStatus() {
		return status;
	}
	public void setStatus(AcdAgentStatus status) {
		this.status = status;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getCallChannelId() {
		return callChannelId;
	}

	public void setCallChannelId(String callChannelId) {
		this.callChannelId = callChannelId;
	}
}
