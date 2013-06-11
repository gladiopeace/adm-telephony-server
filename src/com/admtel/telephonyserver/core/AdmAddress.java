package com.admtel.telephonyserver.core;

import org.apache.log4j.Logger;

public class AdmAddress {
	SigProtocol protocol;
	String destination = null;
	String gateway = null;

	static Logger log = Logger.getLogger(AdmAddress.class);

	static public AdmAddress fromString(String address) {
		AdmAddress result = null;
		try {
			if (address == null)
				return result;
			result = new AdmAddress();
			if (address.startsWith("sip:")) {
				String[] addressItems = address.substring(4).split("@");
				result.protocol = SigProtocol.SIP;
				if (addressItems.length == 2) {
					result.destination = addressItems[0];
					result.gateway = addressItems[1];
				} else if (addressItems.length == 1) {
					result.destination = addressItems[0];
				}
			} else if (address.startsWith("iax2:")) {
				result.protocol = SigProtocol.IAX2;
				String[] addressItems = address.substring(5).split("@");
				if (addressItems.length == 2) {
					result.destination = addressItems[0];
					result.gateway = addressItems[1];
				} else if (addressItems.length == 1) {
					result.destination = addressItems[0];
				}
			} else if (address.startsWith("local:")) {
				result.protocol = SigProtocol.Local;
				String addressItems = address.substring(6);
				result.destination = addressItems;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
		return result;
	}

	public SigProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(SigProtocol protocol) {
		this.protocol = protocol;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
}
