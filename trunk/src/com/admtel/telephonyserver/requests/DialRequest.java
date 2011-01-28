package com.admtel.telephonyserver.requests;

public class DialRequest extends ChannelRequest {
	private String destination;
	private long timeout;

	public DialRequest (String channelId, String destination, long timeout){
		super(RequestType.DialRequest, channelId);
		this.destination = destination;
		this.timeout=timeout;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "DialRequest ["
				+ (destination != null ? "destination=" + destination + ", "
						: "")
				+ "timeout="
				+ timeout
				+ ", "
				+ (super.toString() != null ? "toString()=" + super.toString()
						: "") + "]";
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
