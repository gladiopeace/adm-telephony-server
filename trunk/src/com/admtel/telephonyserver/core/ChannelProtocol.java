package com.admtel.telephonyserver.core;

public enum ChannelProtocol {
	Unknown, ISDN, SIP, H323, IAX2, Local;
	
	public static ChannelProtocol fromString (String protocolStr){
		if (protocolStr == null) return Unknown;
		if (protocolStr.equalsIgnoreCase("sip")){
			return SIP;
		}
		else if (protocolStr.equalsIgnoreCase("h323")){
			return H323;
		}
		else if (protocolStr.equalsIgnoreCase("iax2")){
			return IAX2;
		}
		else if (protocolStr.equals("local")){
			return Local;
		}
		return Unknown;
	}
}
