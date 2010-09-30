package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class ConferenceJoinedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConferenceJoinedEvent [");
		if (conferenceId != null) {
			builder.append("conferenceId=");
			builder.append(conferenceId);
			builder.append(", ");
		}
		builder.append("deaf=");
		builder.append(deaf);
		builder.append(", moderator=");
		builder.append(moderator);
		builder.append(", muted=");
		builder.append(muted);
		builder.append(", ");
		if (participantId != null) {
			builder.append("participantId=");
			builder.append(participantId);
		}
		builder.append("]");
		return builder.toString();
	}

	private String participantId;
	private String conferenceId;
	private boolean moderator;
	private boolean muted;
	private boolean deaf;

	public ConferenceJoinedEvent(Channel channel, String conferenceId, String participantId, boolean moderator, boolean muted, boolean deaf) {
		super(channel);
		this.participantId = participantId;
		this.conferenceId = conferenceId;
		this.moderator = moderator;
		this.muted = muted;
		this.deaf = deaf;
		eventType = EventType.ConferenceJoined;
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

	public boolean isModerator() {
		return moderator;
	}

	public void setModerator(boolean moderator) {
		this.moderator = moderator;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public boolean isDeaf() {
		return deaf;
	}

	public void setDeaf(boolean deaf) {
		this.deaf = deaf;
	}
}
