package com.admtel.telephonyserver.commands;

import java.util.Date;

import org.apache.mina.core.session.IoSession;
import org.naturalcli.Command;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.ParseResult;

public class CommandByeExecutor implements ICommandExecutor {
	IoSession session;
	public CommandByeExecutor(IoSession session){
		this.session = session;
		
	}
	@Override
	public void execute(ParseResult pr) throws ExecutionException {
		session.close(false);
		
	}
}
