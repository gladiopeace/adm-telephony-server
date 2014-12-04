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
		//Option g so that leg a doesn't hang up when leg b hangs up
		String dialStr = String.format("%s,%d,g", destination, timeout); 		
		return String
						.format(
								"Action: AGI\nChannel: %s\nCommand: EXEC DIAL %s\nActionId: %s\nCommandID: %s",
								channel.getId(), dialStr, actionId, actionId);
	}
}
