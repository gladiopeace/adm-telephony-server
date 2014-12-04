package com.admtel.telephonyserver.commands;

import java.util.Set;

import org.apache.mina.core.session.IoSession;
import org.naturalcli.Command;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.ParseResult;

public class CommandHelpExecutor implements ICommandExecutor {
	IoSession session;
	Set<Command> commands;
	public CommandHelpExecutor(IoSession session, Set<Command> commands){
		this.session = session;
		this.commands = commands;
	}
	@Override
	public void execute(ParseResult pr) throws ExecutionException {
		StringBuilder sb = new StringBuilder();
		sb.append("\n>\n");
		for (Command c:commands){
			sb.append(c.getSyntax() + " : "+c.getHelp()+"\n");
		}
		sb.append("\n>");
		session.write(sb.toString());
		
	}
}
