package com.admtel.telephonyserver.asterisk.commands;

import com.admtel.telephonyserver.asterisk.ASTChannel;

public class ASTSetVariableCommand extends ASTCommand {
	private String variableName;
	private String variableValue;

	public ASTSetVariableCommand(ASTChannel channel, String variableName, String variableValue){
		super(channel);
		this.variableName = variableName;
		this.variableValue = variableValue;
	}
	
	public String toString(){
		return String.format(
				"ACTION: SetVar\nChannel: %s\nVariable: %s\nValue: %s\nActionID: %s",
						channel.getId(), variableName, variableValue, channel.getId() + "___SetVar");
						
	}
}
