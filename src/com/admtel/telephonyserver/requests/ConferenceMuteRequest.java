package com.admtel.telephonyserver.requests;

public class ConferenceMuteRequest extends ChannelRequest {
	private boolean mute;

	public ConferenceMuteRequest(String channelId, boolean mute){
		super(RequestType.ConferenceMuteRequest, channelId);
		this.mute = mute;		
	}

	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}

	@Override
	public String toString() {
		return "ParticipantMuteRequest [mute=" + mute + ", "
				+ (channelId != null ? "channelId=" + channelId + ", " : "")
				+ (type != null ? "type=" + type : "") + "]";
	}
}
