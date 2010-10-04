package com.admtel.telephonyserver.cli;

import com.admtel.telephonyserver.requests.Request;

abstract public class CLI_Command{
	
	static String command = "";
	static int minimumArgs = 0;
	
	String[] cmds;
	
	public Request parse(String cmd){
		cmds = cmd.split(":| ");
		if (cmds.length >= minimumArgs){
			return onParse();
		}
		return null;
	}
	
	abstract Request onParse();

}
