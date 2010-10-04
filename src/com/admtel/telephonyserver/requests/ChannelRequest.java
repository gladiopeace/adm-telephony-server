package com.admtel.telephonyserver.requests;

import com.admtel.telephonyserver.requests.Request.RequestType;

public class ChannelRequest extends Request {

	public ChannelRequest(){
		
	}
	@Override
	public String toString() {
		return "ChannelRequest ["
				+ (channelId != null ? "channelId=" + channelId + ", " : "")
				+ (super.toString() != null ? "toString()=" + super.toString()
						: "") + "]";
	}
	String channelId;
	public ChannelRequest(RequestType type, String channelId) {
		super(type);
		this.channelId = channelId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}
