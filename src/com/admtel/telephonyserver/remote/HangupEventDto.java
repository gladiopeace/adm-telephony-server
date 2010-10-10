package com.admtel.telephonyserver.remote;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

@Dto
public class HangupEventDto extends EventDto {
	@DtoField("channel.uniqueId")
	String channelId;
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getHangupCauseStr() {
		return hangupCauseStr;
	}

	public void setHangupCauseStr(String hangupCauseStr) {
		this.hangupCauseStr = hangupCauseStr;
	}

	public int getHangupCause() {
		return hangupCause;
	}

	public void setHangupCause(int hangupCause) {
		this.hangupCause = hangupCause;
	}

	@DtoField("hangupCauseStr")
	String hangupCauseStr;
	
	@DtoField("hangupCause")
	int hangupCause;
}
