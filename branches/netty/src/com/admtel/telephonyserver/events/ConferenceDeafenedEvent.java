package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;


public class ConferenceDeafenedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConferenceDeafenedEvent [conferenceId=")
				.append(conferenceId).append(", participantId=")
				.append(participantId).append(", deafened=").append(deafened)
				.append("]");
		return builder.toString();
	}

	private String conferenceId;
	private String participantId;
	private boolean deafened;

	public ConferenceDeafenedEvent(Channel channel, String conferenceId, String participantId, boolean deafened) {
		super(channel);
		eventType = EventType.ConferenceDeafened;
		this.conferenceId = conferenceId;
		this.participantId = participantId;
		this.deafened = deafened;
	}

	public String getConferenceId() {
		return conferenceId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public boolean isDeafened() {
		return deafened;
	}

	public void setDeafened(boolean deafened) {
		this.deafened = deafened;
	}
}
