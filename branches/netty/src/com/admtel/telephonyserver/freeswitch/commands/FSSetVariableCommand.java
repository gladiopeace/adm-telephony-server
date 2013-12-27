package com.admtel.telephonyserver.freeswitch.commands;

import com.admtel.telephonyserver.freeswitch.FSChannel;



public class FSSetVariableCommand extends FSCommand {

	private String variableName;
	private String variableValue;

	public FSSetVariableCommand(FSChannel channel,String variableName, String variableValue) {
		super(channel);
		this.variableName = variableName;
		this.variableValue = variableValue;
	}
	public String toString(){
		return String
		.format(
				"SendMsg %s\ncall-command: %s\nexecute-app-name: %s\nexecute-app-arg: %s=%s\n",
				channel.getId(), "execute", "set", variableName, variableValue); //TODO more parameters
	}

}
