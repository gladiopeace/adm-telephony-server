package com.admtel.telephonyserver.requests;

import com.admtel.telephonyserver.requests.Request.RequestType;

public class ShowChannelsRequest extends SwitchRequest {

	
	public ShowChannelsRequest(String switchId) {
		super(RequestType.ShowChannelsRequest, switchId);
		
	}

}
