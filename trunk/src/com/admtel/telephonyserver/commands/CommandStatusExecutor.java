package com.admtel.telephonyserver.commands;

import java.text.NumberFormat;

import org.apache.mina.core.session.IoSession;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.ParseResult;

import com.admtel.telephonyserver.core.Switches;

public class CommandStatusExecutor implements ICommandExecutor{
	private IoSession session;

	public CommandStatusExecutor (IoSession session) {
		this.session = session;
	}

	@Override
	public void execute(ParseResult arg0) throws ExecutionException {
		Runtime runtime = Runtime.getRuntime();

	    NumberFormat format = NumberFormat.getInstance();

	    StringBuilder sb = new StringBuilder();
	    long maxMemory = runtime.maxMemory();
	    long allocatedMemory = runtime.totalMemory();
	    long freeMemory = runtime.freeMemory();

		sb.append("\n>\n");
		sb.append("channels : " +Switches.getInstance().getChannelCount());
	    sb.append("free memory: " + format.format(freeMemory / 1024) + "\n");
	    sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "\n");
	    sb.append("max memory: " + format.format(maxMemory / 1024) + "\n");
	    sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "\n");		

		sb.append("\n>");
		session.write(sb.toString());		
	}
}
