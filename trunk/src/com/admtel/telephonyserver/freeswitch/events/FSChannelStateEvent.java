package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSChannelStateEvent extends FSChannelEvent {

	@Override
	public String toString() {
		return "FSChannelStateEvent ["
				+ (channelState != null ? "channelState=" + channelState + ", "
						: "")
				+ (callState != null ? "callState=" + callState : "") + "]";
	}

	public enum ChannelState{CS_NEW, CS_INIT, CS_ROUTING, CS_EXECUTE, CS_CONSUME_MEDIA, CS_HIBERNATE, CS_EXCHANGE_MEDIA, CS_HANGUP, CS_REPORTING, CS_DESTROY};
	public enum CallState{DOWN, RINGING, ACTIVE, HANGUP, EARLY};
	
	
	ChannelState channelState;
	CallState callState;
	
	public ChannelState getChannelState() {
		return channelState;
	}

	public void setChannelState(ChannelState channelState) {
		this.channelState = channelState;
	}

	public CallState getCallState() {
		return callState;
	}

	public void setCallState(CallState callState) {
		this.callState = callState;
	}

	public FSChannelStateEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelState;
		String channelStateStr = (String) values.get("Channel-State");
		channelState = ChannelState.valueOf(channelStateStr);
		callState = CallState.valueOf((String)values.get("Channel-Call-State"));
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	public String getCallerIdNum(){
		return values.get("Caller-Caller-ID-Number");
	}
	public String getCalledIdNum(){
		return values.get("Caller-Destination-Number");
	}
	public String getUserName(){
		return values.get("Caller-Username");
	}
	public String getChannelAddress(){
		return values.get("Caller-Network-Addr");
	}
}
