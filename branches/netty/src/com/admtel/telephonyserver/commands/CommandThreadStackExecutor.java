package com.admtel.telephonyserver.commands;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.ParseResult;

import com.admtel.telephonyserver.core.ScriptManager;
import com.admtel.telephonyserver.core.StatsManager;
import com.admtel.telephonyserver.core.Switches;

public class CommandThreadStackExecutor implements ICommandExecutor{
	private IoSession session;

	public CommandThreadStackExecutor (IoSession session) {
		this.session = session;
	}

	@Override
	public void execute(ParseResult arg0) throws ExecutionException {
		
		Map<Thread, StackTraceElement[]> trace = Thread.getAllStackTraces();
		
	    StringBuilder sb = new StringBuilder();

	    
		sb.append("\n>\n");
		Iterator<Map.Entry<Thread, StackTraceElement[]>> it = trace.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Thread, StackTraceElement[]> pairs = (Map.Entry<Thread, StackTraceElement[]>)it.next();
	        sb.append("Thread : " + pairs.getKey().getId()+"\n");
	        StackTraceElement[] stack = pairs.getValue();
	        for (StackTraceElement s:stack) {
	        	sb.append(String.format("\t%s:%s:%d\n", s.getClassName(), s.getMethodName(), s.getLineNumber()));
	        }
	    }
		sb.append("\n>");
		session.write(sb.toString());		
	}
}
