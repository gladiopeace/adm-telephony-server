package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;


public class ConferenceMutedEvent extends ChannelEvent {

	private String conferenceId;
	private String participantId;
	private boolean muted;

	public ConferenceMutedEvent(Channel channel, String conferenceId, String participantId, boolean muted) {
		super(channel);
		eventType = EventType.ConferenceMuted;
		this.conferenceId = conferenceId;
		this.participantId = participantId;
		this.muted = muted;
	}

	public boolean isMuted() {
		return muted;
	}

	public String getConferenceId() {
		return conferenceId;
	}

	public String getParticipantId() {
		return participantId;
	}
}
