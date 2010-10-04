package com.admtel.telephonyserver.requests;

import com.admtel.telephonyserver.requests.Request.RequestType;

public class AnswerRequest extends ChannelRequest {

	public AnswerRequest(String channelId) {
		super(RequestType.AnswerRequest, channelId);	
	}

}
