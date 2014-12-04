package com.admtel.telephonyserver.asterisk.commands;

import com.admtel.telephonyserver.asterisk.ASTChannel;

public class ASTMeetmeUnmuteCommand extends ASTCommand {

	private String meetme;
	private String usernum;

	public ASTMeetmeUnmuteCommand(ASTChannel channel, String meetme, String usernum) {
		super(channel);
		this.meetme = meetme;
		this.usernum = usernum;
	}

	@Override
	public String toString() {
		return String.format(
				"ACTION: MeetmeUnmute\nMeetme: %s\nusernum: %s\nActionID: %s", 
				meetme, usernum, channel.getId() + "___MeetmeMute");
}
}
