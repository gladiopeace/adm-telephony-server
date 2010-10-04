package com.admtel.telephonyserver.requests;

import com.admtel.telephonyserver.core.DisconnectCode;

public class HangupRequest extends ChannelRequest {
    DisconnectCode disconnectCode;

    public HangupRequest(){
    	
    }
	public HangupRequest(String channelId) {
		super(RequestType.HangupRequest, channelId);		
		this.disconnectCode = DisconnectCode.Normal;
	}
	
	@Override
	public String toString() {
		return "HangupRequest [disconnectCode="
				+ disconnectCode
				+ ", "
				+ (super.toString() != null ? "toString()=" + super.toString()
						: "") + "]";
	}

	public HangupRequest( String channelId, DisconnectCode disconnectCode) {
		super(RequestType.HangupRequest, channelId);		
		this.disconnectCode = disconnectCode;
	}

	public DisconnectCode getDisconnectCode() {
		return this.disconnectCode;
	}
	public void setDisconnectCode(DisconnectCode disconnectCode) {
		this.disconnectCode = disconnectCode;
	}
}
