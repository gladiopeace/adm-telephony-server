package com.admtel.telephonyserver.asterisk.commands;

import com.admtel.telephonyserver.asterisk.ASTChannel;

public class ASTMeetmeMuteCommand extends ASTCommand {

	private String meetme;
	private String user;

	public ASTMeetmeMuteCommand(ASTChannel channel, String meetme, String user) {
		super(channel);
		this.meetme = meetme;
		this.user = user;
	}

	@Override
	public String toString() {
			return String.format(
					"ACTION: MeetmeMute\nMeetme: %s\nusernum: %s\nActionID: %s", 
					meetme, user, channel.getId() + "___MeetmeMute");
	}
	

}
