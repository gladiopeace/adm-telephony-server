package com.admtel.telephonyserver.cli;

import com.admtel.telephonyserver.core.DisconnectCode;
import com.admtel.telephonyserver.requests.HangupRequest;
import com.admtel.telephonyserver.requests.Request;

public class CLI_HangupCommand extends CLI_Command {
	static String command = "hangup";
	static int miniumArgs = 2;

	@Override
	Request onParse() {
		HangupRequest result = null;
		if (cmds.length == 2){
			result = new HangupRequest(cmds[1]);
		}
		else{
			result = new HangupRequest(cmds[1], DisconnectCode.valueOf(cmds[2]));
		}
		return result;
	}
}
