package com.admtel.telephonyserver.freeswitch.commands;

import com.admtel.telephonyserver.freeswitch.FSChannel;


public class FSMemberMuteCommand extends FSCommand {

	private String conferenceId;
	private String memberId;
	private boolean mute;

	public FSMemberMuteCommand(FSChannel channel, String conferenceId, String memberId, boolean mute) {
		super(channel);
		this.conferenceId = conferenceId;
		this.memberId = memberId;
		this.mute = mute;
	}
	public String toString(){
		if (mute){
			return String.format("bgapi conference %s mute %s", conferenceId, memberId);
		}
		else{
			return String.format("bgapi conference %s unmute %s", conferenceId, memberId);
		}
		
//		return String
//		.format(
//				"SendMsg %s\ncall-command: %s\nexecute-app-name: %s\nexecute-app-arg: %s %s\n",
//				channel.getId(), "execute", "fifo", queueName,(isAgent?"out wait":"in")); 
	}
}
