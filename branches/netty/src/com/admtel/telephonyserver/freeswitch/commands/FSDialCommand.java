package com.admtel.telephonyserver.freeswitch.commands;

import com.admtel.telephonyserver.freeswitch.FSChannel;



public class FSDialCommand extends FSCommand {

	private String address;
	private Long timeout;
	private boolean secure;

	public FSDialCommand(FSChannel channel,String address, Long timeout, boolean secure) {
		super(channel);
		this.address = address;
		this.timeout = timeout;
		this.secure = secure;
	}
	public String toString(){
		return String
		.format(
				"SendMsg %s\ncall-command: %s\nexecute-app-name: %s\nexecute-app-arg: [leg_timeout=%d,sip_secure_media=%b, sip_contact_user=%s]%s\n",
				channel.getId(), "execute", "bridge", timeout/1000, secure, channel.getCallingStationId(), address); //TODO more parameters
	}

}
