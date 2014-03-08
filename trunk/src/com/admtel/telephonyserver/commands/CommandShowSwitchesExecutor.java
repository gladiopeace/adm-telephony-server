package com.admtel.telephonyserver.commands;


import java.util.Collection;

import org.apache.mina.core.session.IoSession;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.ParseResult;

import com.admtel.telephonyserver.core.Switch;
import com.admtel.telephonyserver.core.Switches;

public class CommandShowSwitchesExecutor implements ICommandExecutor {
	IoSession session;
	public CommandShowSwitchesExecutor(IoSession session){
		this.session = session;
		
	}
	@Override
	public void execute(ParseResult pr) throws ExecutionException {
		Collection<Switch> switches = Switches.getInstance().getAll();
		StringBuilder sb = new StringBuilder();
		sb.append("\n>\n");
		for (Switch _switch:switches) {
			sb.append(_switch.toReadableString());
		}
		sb.append("\n>");
		session.write(sb.toString());
	}
}
