package com.admtel.telephonyserver.remote;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

@Dto
public class OutboundAlertingEventDto extends EventDto{
	@DtoField("channel.uniqueId")
	String channelId;
	@DtoField("calledNumber")
	String calledNumber;
	@DtoField("callerId")
	String callerId;
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getCalledNumber() {
		return calledNumber;
	}
	public void setCalledNumber(String calledNumber) {
		this.calledNumber = calledNumber;
	}
	public String getCallerId() {
		return callerId;
	}
	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	@Override
	public String toDisplayString() {
		// TODO Auto-generated method stub
		return null;
	}

}
