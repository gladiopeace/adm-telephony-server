package com.admtel.telephonyserver.requests;

public class HangupRequest extends ChannelRequest {
	public HangupRequest(String switchId, String channelId) {
		super(RequestType.HangupRequest, switchId, channelId);		
	}
}
