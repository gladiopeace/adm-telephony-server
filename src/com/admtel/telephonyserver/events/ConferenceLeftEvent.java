package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class ConferenceLeftEvent extends ChannelEvent {

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
