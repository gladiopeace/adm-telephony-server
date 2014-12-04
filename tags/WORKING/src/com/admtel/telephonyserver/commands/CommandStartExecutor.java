package com.admtel.telephonyserver.commands;

import org.apache.mina.core.session.IoSession;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.ParseResult;

import com.admtel.telephonyserver.core.Switches;

public class CommandStartExecutor implements ICommandExecutor {
	IoSession session;
	public CommandStartExecutor(IoSession session){
		this.session = session;
		
	}
	@Override
	public void execute(ParseResult pr) throws ExecutionException {
		Switches.getInstance().start();
	}
}
