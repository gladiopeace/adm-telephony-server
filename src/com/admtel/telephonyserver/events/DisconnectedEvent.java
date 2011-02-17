package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class DisconnectedEvent extends ChannelEvent {

	@Override
	public String toString() {
		return String.format(
				"\t\n\teventType=%s\n\tchannel=%s\n\tdisconnectCode=%s",
				eventType, channel, disconnectCode);
	}

	DisconnectCode disconnectCode;
	public DisconnectedEvent(Channel channel, DisconnectCode disconnectCode) {
		super(channel);
		eventType = EventType.Disconnected;		
		this.disconnectCode = disconnectCode;
	}
	public DisconnectCode getDisconnectCode() {
		return disconnectCode;
	}
}
