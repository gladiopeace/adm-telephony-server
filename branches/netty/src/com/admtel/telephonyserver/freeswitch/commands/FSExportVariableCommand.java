package com.admtel.telephonyserver.freeswitch.commands;

import com.admtel.telephonyserver.freeswitch.FSChannel;



public class FSExportVariableCommand extends FSCommand {

	private String variableName;

	public FSExportVariableCommand(FSChannel channel,String variableName) {
		super(channel);
		this.variableName = variableName;
	}
	public String toString(){
		return String
		.format(
				"SendMsg %s\ncall-command: %s\nexecute-app-name: %s\nexecute-app-arg: %s\n",
				channel.getId(), "execute", "export", variableName); //TODO more parameters
	}

}
