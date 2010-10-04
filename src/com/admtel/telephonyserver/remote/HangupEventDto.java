package com.admtel.telephonyserver.remote;

import dp.lib.dto.geda.annotations.DtoField;

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

	public int getHangCause() {
		return hangCause;
	}

	public void setHangCause(int hangCause) {
		this.hangCause = hangCause;
	}

	@DtoField("hangupCauseStr")
	String hangupCauseStr;
	
	@DtoField("hangupCause")
	int hangCause;
}
