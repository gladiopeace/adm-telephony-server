package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class ConferenceTalkEvent extends ChannelEvent {

	boolean talking;
	String conferenceId;
	String participantId;
	
	public ConferenceTalkEvent(Channel channel, String conferenceId, String participantId, boolean talking) {
		super(channel);
		eventType = EventType.ConferenceTalk;
		this.talking = talking;
		this.conferenceId = conferenceId;
		this.participantId = participantId;
	}
	public String getConferenceId() {
		return conferenceId;
	}
	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	public void setTalking(boolean talking) {
		this.talking = talking;
	}
	public boolean isTalking(){
		return this.talking;
	}
}
