package com.admtel.telephonyserver.core;

public enum SigProtocol {
	Unknown{
		public String toString(){
			return "Unknown";
		}
	}, 
	ISDN{
		public String toString(){
			return "isdn";
		}
	}, 
	SIP{
		public String toString(){
			return "sip";
		}
	}, 
	H323{
		public String toString(){
			return "h323";
		}
	}, 
	IAX2{
		public String toString(){
			return "iax2";
		}
	}, 
	Local{
		public String toString(){
			return "local";
		}
	};
	
	public static SigProtocol fromString (String protocolStr){
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
