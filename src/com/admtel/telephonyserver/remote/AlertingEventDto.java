package com.admtel.telephonyserver.remote;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

@Dto
public class AlertingEventDto extends EventDto{
	@DtoField("channel.uniqueId")
	String channelId;
	
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	@Override
	public String toDisplayString() {
		return String.format("Incoming call : channel(%s)", channelId);
	}
}
