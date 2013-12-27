package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DialFailedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DialFailedEvent [");
		if (channel != null) {
			builder.append("channel=");
			builder.append(channel);
			builder.append(", ");
		}
		if (eventType != null) {
			builder.append("eventType=");
			builder.append(eventType);
			builder.append(", ");
		}
		if (dialStatus != null) {
			builder.append("dialStatus=");
			builder.append(dialStatus);
		}
		builder.append("]");
		return builder.toString();
	}
	DialStatus dialStatus = DialStatus.Unknown;
	
	public DialFailedEvent(Channel channel) {
		super(channel);
		eventType = EventType.DialFailed;
	}
	public DialFailedEvent (Channel channel, DialStatus dialStatus){
		super(channel);
		eventType = EventType.DialFailed;
		this.dialStatus = dialStatus;
	}
	public DialStatus getDialStatus() {
		return dialStatus;
	}
	public void setDialStatus(DialStatus dialStatus) {
		this.dialStatus = dialStatus;
	}
}
