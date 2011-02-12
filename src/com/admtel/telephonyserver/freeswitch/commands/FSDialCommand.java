package com.admtel.telephonyserver.freeswitch.commands;

import com.admtel.telephonyserver.freeswitch.FSChannel;



public class FSDialCommand extends FSCommand {

	private String address;
	private Long timeout;

	public FSDialCommand(FSChannel channel,String address, Long timeout) {
		super(channel);
		this.address = address;
		this.timeout = timeout;
	}
	public String toString(){
		return String
		.format(
				"SendMsg %s\ncall-command: %s\nexecute-app-name: %s\nexecute-app-arg: [leg_timeout=%d]%s\n",
				channel.getId(), "execute", "bridge", timeout/1000, address); //TODO more parameters
	}

}
