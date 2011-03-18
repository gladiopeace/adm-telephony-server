package com.admtel.telephonyserver.requests;

import java.util.HashMap;
import java.util.Map;


public class ChannelRequest extends Request {

	Map<String, String> userData = new HashMap<String, String>();
	
	public Map<String, String> getUserData() {
		return userData;
	}
	public void setUserData(Map<String, String> userData) {
		this.userData = userData;
	}
	public void setUserData (String key, String value){
		userData.put(key, value);
	}
	public ChannelRequest(){
		
	}
	@Override
	public String toString() {
		return "ChannelRequest ["
				+ (channelId != null ? "channelId=" + channelId + ", " : "")
				+ (super.toString() != null ? "toString()=" + super.toString()
						: "") + "]";
	}
	String channelId;
	public ChannelRequest(RequestType type, String channelId) {
		super(type);
		this.channelId = channelId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}
