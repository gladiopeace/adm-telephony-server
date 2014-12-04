package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSChannelHangupEvent extends FSChannelEvent {

	FSHangupCause hangupCause = FSHangupCause.NONE;

	public FSChannelHangupEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelHangup;
		try {
			hangupCause = FSHangupCause.valueOf((String) values
					.get("Hangup-Cause"));
		} catch (Exception e) {

		}
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}

	public FSHangupCause getHangupCause() {
		return hangupCause;
	}

	@Override
	public String toString() {
		return String
				.format("\t\n\tgetSwitchId()=%s\n\tgetEventType()=%s\n\thangupCause=%s",
						getSwitchId(), getEventType(), hangupCause);
	}
}
