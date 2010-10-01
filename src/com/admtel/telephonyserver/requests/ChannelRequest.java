package com.admtel.telephonyserver.requests;

import com.admtel.telephonyserver.requests.Request.RequestType;

public class ChannelRequest extends SwitchRequest {

	String channelId;
	public ChannelRequest(RequestType type, String switchId, String channelId) {
		super(type, switchId);
		this.channelId = channelId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}
