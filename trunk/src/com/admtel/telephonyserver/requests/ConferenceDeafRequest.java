package com.admtel.telephonyserver.requests;

public class ConferenceDeafRequest extends ChannelRequest {
	private boolean deaf;

	public ConferenceDeafRequest(String channelId, boolean deaf){
		super(RequestType.ConferenceDeafRequest, channelId);
		this.deaf = deaf;		
	}

	public boolean isDeaf() {
		return deaf;
	}

	public void setDeaf(boolean deaf) {
		this.deaf = deaf;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConferenceDeafRequest [deaf=").append(deaf).append("]");
		return builder.toString();
	}

}
