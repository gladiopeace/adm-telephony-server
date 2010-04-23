package com.admtel.telephonyserver.events.freeswitch;

public enum FSChannelState {
	Unknow, Ringing, Answer;
	public static FSChannelState fromString(String answerState){
		if (answerState.equals("ringing")){
			return FSChannelState.Ringing;
		}
		else if (answerState.equals("answer")){
			return FSChannelState.Answer;
		}
		return FSChannelState.Unknow;
	}
}
