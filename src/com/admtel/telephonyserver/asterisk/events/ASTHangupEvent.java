package com.admtel.telephonyserver.asterisk.events;

import java.util.HashMap;
import java.util.Map;

import com.admtel.telephonyserver.events.DisconnectCode;

public class ASTHangupEvent extends ASTChannelEvent {

	@Override
	public String toString() {
		return "ASTHangupEvent [getCause()=" + getCause() + ", getCauseTxt()="
				+ getCauseTxt() + "]";
	}

	public ASTHangupEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.Hangup;
	}

	public int getCause() {
		try {
			return Integer.parseInt(values.get("Cause"));
		} catch (Exception e) {

		}
		return 0;
	}

	public String getCauseTxt() {
		return values.get("Cause-txt");
	}
	public static ASTHangupEvent build(String switchId, String channel, DisconnectCode disconnectCode) {
		Map<String, String> v = new HashMap<String,String>();
		v.put("Cause", String.valueOf(disconnectCode.ordinal()));
		v.put("Channel", channel);
		return new ASTHangupEvent(switchId, v);
	}
}
