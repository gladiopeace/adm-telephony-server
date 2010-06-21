package com.admtel.telephonyserver.asterisk.commands;

import com.admtel.telephonyserver.asterisk.ASTChannel;

public class ASTGetVariableCommand extends ASTCommand {
	private String variableName;

	public ASTGetVariableCommand (ASTChannel channel, String variableName){
		super (channel);
		this.variableName = variableName;
	}
	
	public String toString(){
		return String.format(
				"ACTION: GetVar\nChannel: %s\nVariable: %s\nActionID: %s", 
				channel.getId(), variableName, channel.getId() + "___GetVar");
	}
}
