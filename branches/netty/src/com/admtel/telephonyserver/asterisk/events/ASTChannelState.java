package com.admtel.telephonyserver.asterisk.events;

public enum ASTChannelState {
	Uknown, Ringing, Ring, Answer;
	
	public static ASTChannelState fromString(String channelState){
		
		if (channelState == null) return ASTChannelState.Uknown;
		
		if (channelState.equals("5")){ //outgoing
			return ASTChannelState.Ringing;
		}
		if (channelState.equals("4")){
			return ASTChannelState.Ring; // Incoming
		}
		if (channelState.equals("6")){
			return ASTChannelState.Answer;
		}
		return ASTChannelState.Uknown;
	}
}
