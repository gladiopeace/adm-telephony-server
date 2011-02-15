package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DialFailedEvent extends ChannelEvent {

	@Override
	public String toString() {
		return "[" + (channel != null ? "channel=" + channel + ", " : "")
				+ (eventType != null ? "eventType=" + eventType + ", " : "")
				+ (dialStatus != null ? "dialStatus=" + dialStatus : "") + "]";
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
