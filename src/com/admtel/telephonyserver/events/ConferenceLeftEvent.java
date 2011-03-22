package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class ConferenceLeftEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConferenceLeftEvent [");
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
		if (participantId != null) {
			builder.append("participantId=");
			builder.append(participantId);
			builder.append(", ");
		}
		if (conferenceId != null) {
			builder.append("conferenceId=");
			builder.append(conferenceId);
		}
		builder.append("]");
		return builder.toString();
	}

	private String participantId;
	private String conferenceId;

	public ConferenceLeftEvent(Channel channel, String conferenceId, String participantId) {
		super(channel);
		eventType = EventType.ConferenceLeft;
		this.participantId = participantId;
		this.conferenceId = conferenceId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getConferenceId() {
		return conferenceId;
	}

	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}
}
