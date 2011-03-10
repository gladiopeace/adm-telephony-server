package com.admtel.telephonyserver.events;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DisconnectCode {
	None(0),
	UnallocatedNumber(1), 
	NoRoute(2), 
	NoRouteToDestination(3), 
	SendSIT(4), 
	MisdialedTrunkPrefix(5), 
	ChannelUnacceptable(6), 
	Preemption(8), 
	Normal(16), 
	UserBusy(17), 
	NoResponse(18), 
	NoAnswer(19), 
	SubscriberAbsent(20), 
	CallRejected(21), 
	NumberChanged(22), 
	NonSelectedUserClearing(26), 
	DestinationOutOfOrder(27), 
	InvalidNumberFormat(28), 
	FacilityRejected(29), 
	Unspecified(31), 
	NoCircuitAvailable(34), 
	NetworkOutOfOrder(38), 
	TemporaryFailure(41), 
	Congestion(42), 
	RequestChannelUnavailable(44), 
	ResourceUnavailable(47), 
	QualityOfServiceNotAvailable(49), ;

	private static final Map<Integer, DisconnectCode> lookup = new HashMap<Integer, DisconnectCode>();

	static {
		for (DisconnectCode s : EnumSet.allOf(DisconnectCode.class))
			lookup.put(s.toInteger(), s);
	}
	private final int code;

	DisconnectCode(int code) {
		this.code = code;
	}

	public Integer toInteger() {
		return code;
	}
	 public static DisconnectCode get(int code) { 
		 DisconnectCode dc = lookup.get(code);
         if (dc == null) return None;
         
         return dc;
    }
}
