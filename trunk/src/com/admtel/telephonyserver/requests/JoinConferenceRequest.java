package com.admtel.telephonyserver.requests;

import com.admtel.telephonyserver.requests.Request.RequestType;

public class JoinConferenceRequest extends ChannelRequest {
	private String conference;
	private boolean moderator;
	private boolean muted;
	private boolean deaf;

	public JoinConferenceRequest (String channelId, String conference, boolean moderator, boolean muted, boolean deaf){
		super(RequestType.JoinConferenceRequest, channelId);
		this.conference = conference;
		this.moderator = moderator;
		this.muted = muted;
		this.deaf = deaf;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
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

	@Override
	public String toString() {
		return String
				.format("\t\n\tchannelId=%s\n\ttype=%s\n\tconference=%s\n\tmoderator=%s\n\tmuted=%s\n\tdeaf=%s",
						channelId, type, conference, moderator, muted, deaf);
	}
}
