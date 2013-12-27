package com.admtel.telephonyserver.acd;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AcdAgentStatus {

	NotReady(0), Ready(1), Busy(2), ;
	private static final Map<Integer, AcdAgentStatus> lookup = new HashMap<Integer, AcdAgentStatus>();

	static {
		for (AcdAgentStatus s : EnumSet.allOf(AcdAgentStatus.class))
			lookup.put(s.toInteger(), s);
	}
	private final int code;

	AcdAgentStatus(int code) {
		this.code = code;
	}

	public int toInteger() {
		return this.code;
	}
	public static AcdAgentStatus get(int code) { 
        return lookup.get(code); 
   }
}
