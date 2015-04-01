package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

import com.admtel.telephonyserver.config.SwitchType;
import com.admtel.telephonyserver.core.SigProtocol;

public class ASTPeerStatusEvent extends ASTEvent {

	Boolean registered = null;
	String user = null;
	SigProtocol sigProtocol = SigProtocol.Unknown;
	String address;
	
	public ASTPeerStatusEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.PeerStatus;
		sigProtocol = SigProtocol.fromString(values.get("ChannelType"), SwitchType.Asterisk);
	}
	public SigProtocol getSigProtocol() {
		return sigProtocol;
	}
	public String getUser(){
		if (user == null){
			String peerStr = values.get("Peer");
			if (peerStr != null){
				String channelTypeStr = values.get("ChannelType");
				if (channelTypeStr != null){
					user = peerStr.substring(channelTypeStr.length()+1);
				}
			}
		}
		return user;
	}
	public Boolean getRegistered(){
		if (registered == null){
			String peerStatusStr = values.get("PeerStatus");
			if (peerStatusStr != null){
				if (peerStatusStr.equals("Registered")){
					registered = true;
				}
				else{
					registered = false;
				}
			}
		}
		return registered;
	}
	public SigProtocol getProtocol() {
		return this.sigProtocol;
	}
	public String getAddress(){
		return values.get("Address");
	}
	

}
