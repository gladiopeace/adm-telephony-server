package com.admtel.telephonyserver.commands;

import org.apache.mina.core.session.IoSession;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.ParseResult;

public class CommandGarbageCollectExecutor implements ICommandExecutor {
	IoSession session;
	public CommandGarbageCollectExecutor(IoSession session){
		this.session = session;
		
	}
	@Override
	public void execute(ParseResult pr) throws ExecutionException {
		System.gc();		
	}
}
