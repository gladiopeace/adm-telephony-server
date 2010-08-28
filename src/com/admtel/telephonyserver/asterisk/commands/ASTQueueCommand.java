package com.admtel.telephonyserver.asterisk.commands;

import com.admtel.telephonyserver.asterisk.ASTChannel;

public class ASTQueueCommand extends ASTCommand {

	private String queueName;

	public ASTQueueCommand(ASTChannel channel, String queueName) {
		super(channel);
		this.queueName = queueName;
	}
	public String toString(){
		
			String actionId = channel.getId() + "___Queue";
			String queueStr = String.format("%s", queueName);
			return String
							.format(
									"Action: AGI\nChannel: %s\nCommand: EXEC QUEUE %s\nActionId: %s\nCommandID: %s",
									channel.getId(), queueStr, actionId, actionId);

	}
}
