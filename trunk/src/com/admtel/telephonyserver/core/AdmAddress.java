package com.admtel.telephonyserver.core;


public class AdmAddress {
	SigProtocol protocol;
	String destination = null;
	String gateway = null;
	
	
	public AdmAddress(String address){
		if (address.startsWith("sip:")){
			String[] addressItems = address.substring(4).split("@");
			protocol = SigProtocol.SIP;
			if (addressItems.length == 2){				
				destination = addressItems[0];
				gateway = addressItems[1];				
			}
			else if (addressItems.length==1){
				destination = addressItems[0];
			}
		}
		else if (address.startsWith("iax2:")){
			protocol = SigProtocol.IAX2;
			String[] addressItems = address.substring(5).split("@");
			if (addressItems.length == 2){
				destination = addressItems[0];
				gateway = addressItems[1];				
			}
			else if (addressItems.length==1){
				destination = addressItems[0];
			}
		}
		else if (address.startsWith("local:")){
			protocol = SigProtocol.Local;
			String addressItems = address.substring(5);
			destination = addressItems;
		}
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
