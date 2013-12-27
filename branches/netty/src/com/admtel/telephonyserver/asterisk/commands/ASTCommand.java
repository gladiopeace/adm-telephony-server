package com.admtel.telephonyserver.asterisk.commands;

import com.admtel.telephonyserver.asterisk.ASTChannel;

public class ASTCommand {
	protected ASTChannel channel;

	public ASTCommand (ASTChannel channel){
		this.channel = channel;
	}
}
