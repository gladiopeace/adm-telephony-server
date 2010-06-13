package com.admtel.telephonyserver.events.asterisk;

import java.util.Map;

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
	public String getCause(){
		return values.get("Cause");
	}
	public String getCauseTxt(){
		return values.get("Cause-txt");
	}
}
