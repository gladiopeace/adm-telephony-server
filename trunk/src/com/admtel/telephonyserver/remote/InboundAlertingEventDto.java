package com.admtel.telephonyserver.remote;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

@Dto
public class InboundAlertingEventDto extends EventDto{
	@DtoField("channel.uniqueId")
	String channelId;
	@DtoField("calledIdNumber")
	String calledIdNumber;
	@DtoField("callerIdNumber")
	String callerIdNumber;
	@DtoField("callerIdName")
	String callerIdName;
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getCalledIdNumber() {
		return calledIdNumber;
	}
	public void setCalledIdNumber(String calledIdNumber) {
		this.calledIdNumber = calledIdNumber;
	}
	public String getCallerIdNumber() {
		return callerIdNumber;
	}
	public void setCallerIdNumber(String callerIdNumber) {
		this.callerIdNumber = callerIdNumber;
	}
	public String getCallerIdName() {
		return callerIdName;
	}
	public void setCallerIdName(String callerIdName) {
		this.callerIdName = callerIdName;
	}
	@Override
	public String toDisplayString() {
		return String.format("Incoming call : channel(%s), calledId(%s), callerId(%s), callerName(%s)", channelId, calledIdNumber, callerIdNumber, callerIdName);
	}
}
