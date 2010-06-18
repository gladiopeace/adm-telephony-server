package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class ASTPeerStatusEvent extends ASTEvent {

	Boolean registered = null;
	String user = null;
	public ASTPeerStatusEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.PeerStatus;
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
	

}
