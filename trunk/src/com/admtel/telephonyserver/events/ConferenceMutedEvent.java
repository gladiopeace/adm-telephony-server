package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;


public class ConferenceMutedEvent extends ChannelEvent {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConferenceMutedEvent [");
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
		if (conferenceId != null) {
			builder.append("conferenceId=");
			builder.append(conferenceId);
			builder.append(", ");
		}
		if (participantId != null) {
			builder.append("participantId=");
			builder.append(participantId);
			builder.append(", ");
		}
		builder.append("muted=");
		builder.append(muted);
		builder.append("]");
		return builder.toString();
	}

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
