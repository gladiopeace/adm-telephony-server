package com.admtel.telephonyserver.asterisk.commands;

import com.admtel.telephonyserver.asterisk.ASTChannel;

public class ASTDialCommand extends ASTCommand {
	private String destination;
	private Long timeout;

	public ASTDialCommand (ASTChannel channel, String destination, Long timeout){
		super(channel);
		this.destination = destination;
		this.timeout = timeout;
	}
	public String toString (){
		String actionId = channel.getId() + "___Dial";
		String dialStr = String.format("%s||%d", destination, timeout);
		return String
						.format(
								"Action: AGI\nChannel: %s\nCommand: EXEC DIAL %s\nActionId: %s\nCommandID: %s",
								channel.getId(), dialStr, actionId, actionId);
	}
}
