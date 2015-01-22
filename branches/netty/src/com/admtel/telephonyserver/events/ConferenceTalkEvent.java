package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class ConferenceTalkEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConferenceTalkEvent [");
		if (channel != null) {
			builder.append("channel=");
			builder.append(channel);
			builder.append(", ");
		}
		if (eventType != null) {
			builder.append("eventType=");
			builder.append(eventType);
			builder.append(", ");
		}
		builder.append("talking=");
		builder.append(talking);
		builder.append(", ");
		if (conferenceId != null) {
			builder.append("conferenceId=");
			builder.append(conferenceId);
			builder.append(", ");
		}
		if (participantId != null) {
			builder.append("participantId=");
			builder.append(participantId);
		}
		builder.append("]");
		return builder.toString();
	}
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
