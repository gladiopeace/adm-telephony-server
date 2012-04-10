package com.admtel.telephonyserver.freeswitch.commands;

import com.admtel.telephonyserver.freeswitch.FSChannel;


public class FSMemberDeafCommand extends FSCommand {

	private String conferenceId;
	private String memberId;
	private boolean deaf;

	public FSMemberDeafCommand(FSChannel channel, String conferenceId, String memberId, boolean deaf) {
		super(channel);
		this.conferenceId = conferenceId;
		this.memberId = memberId;
		this.deaf = deaf;
	}
	public String toString(){
		if (deaf){
			return String.format("bgapi conference %s deaf %s", conferenceId, memberId);
		}
		else{
			return String.format("bgapi conference %s undeaf %s", conferenceId, memberId);
		}
		
//		return String
//		.format(
//				"SendMsg %s\ncall-command: %s\nexecute-app-name: %s\nexecute-app-arg: %s %s\n",
//				channel.getId(), "execute", "fifo", queueName,(isAgent?"out wait":"in")); 
	}
}
