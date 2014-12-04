package com.admtel.telephonyserver.freeswitch.commands;

import com.admtel.telephonyserver.freeswitch.FSChannel;

public class FSCommand {
	protected FSChannel channel;
	
	public FSCommand(FSChannel channel){
		this.channel = channel;
	}
	
}
