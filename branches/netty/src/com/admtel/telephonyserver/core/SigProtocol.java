package com.admtel.telephonyserver.core;

import com.admtel.telephonyserver.config.SwitchType;

public enum SigProtocol {
	Unknown {
		public String toString() {
			return "Unknown";
		}
	},
	ISDN {
		public String toString() {
			return "isdn";
		}
	},
	SIP {
		public String toString() {
			return "sip";
		}
	},
	H323 {
		public String toString() {
			return "h323";
		}
	},
	IAX2 {
		public String toString() {
			return "iax2";
		}
	},
	Local {
		public String toString() {
			return "local";
		}
	};

	public static SigProtocol fromString (String protocol){
		SigProtocol sigProtocol = Unknown;
		if (protocol.equalsIgnoreCase("sip")){
			sigProtocol = SIP;
		}
		else if (protocol.equalsIgnoreCase("iax2")){
			sigProtocol = IAX2;
		}
		else if (protocol.equalsIgnoreCase("local")){
			sigProtocol = Local;
		}
		else if (protocol.equalsIgnoreCase("h323")){
			sigProtocol = H323;
		}
		else if (protocol.equalsIgnoreCase("isdn")){
			sigProtocol = ISDN;
		}
		return sigProtocol;
	}
	public static SigProtocol fromString(String protocolStr,
			SwitchType switchType) {
		if (protocolStr == null)
			return Unknown;
		if (switchType == SwitchType.Freeswitch) {
			if (protocolStr.equalsIgnoreCase("sip")) {
				return SIP;
			} else if (protocolStr.equalsIgnoreCase("h323")) {
				return H323;
			} else if (protocolStr.equalsIgnoreCase("iax2")) {
				return IAX2;
			} else if (protocolStr.equals("local")) {
				return Local;
			}
		} else if (switchType == SwitchType.Asterisk) {
			if (protocolStr.equalsIgnoreCase("SIP")) {
				return SIP;
			} else if (protocolStr.equalsIgnoreCase("OH323")) {
				return H323;
			} else if (protocolStr.equalsIgnoreCase("IAX2")) {
				return IAX2;
			} else if (protocolStr.equals("Local")) {
				return Local;
			}
		}
		return Unknown;
	}
}
