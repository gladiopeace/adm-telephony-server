package com.admtel.telephonyserver.cli;

import com.admtel.telephonyserver.requests.ReloadRequest;
import com.admtel.telephonyserver.requests.Request;

public class CLI_ReloadCommand extends CLI_Command {
	static String command = "reload";
	static int miniumArgs = 1;
	
	@Override
	Request onParse() {
		return new ReloadRequest();
	}

}
