package com.admtel.telephonyserver.events;

import com.admtel.telephonyserver.core.Channel;

public class ConferenceJoinedEvent extends ChannelEvent {

	private String participantId;
	private boolean moderator;
	private boolean muted;
	private boolean deaf;

	public ConferenceJoinedEvent(Channel channel, String participantId, boolean moderator, boolean muted, boolean deaf) {
		super(channel);
		this.participantId = participantId;
		this.moderator = moderator;
		this.muted = muted;
		this.deaf = deaf;
		eventType = EventType.ConferenceJoined;
	}

}
