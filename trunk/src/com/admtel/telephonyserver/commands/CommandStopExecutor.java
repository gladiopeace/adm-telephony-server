package com.admtel.telephonyserver.commands;

import org.apache.mina.core.session.IoSession;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.ParseResult;

import com.admtel.telephonyserver.core.Switches;

public class CommandStopExecutor implements ICommandExecutor {
	IoSession session;
	public CommandStopExecutor(IoSession session){
		this.session = session;
		
	}
	@Override
	public void execute(ParseResult pr) throws ExecutionException {
		if (pr.getParameterCount() > 0) {
			String force = (String) pr.getParameterValue(0);
			if (force != null && force.equalsIgnoreCase("now")) {
				Switches.getInstance().stop(true);
			}			
		}		
		else {
			Switches.getInstance().stop(false);
		}
		
	}
}
