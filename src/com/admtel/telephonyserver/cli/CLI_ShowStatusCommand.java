package com.admtel.telephonyserver.cli;

import com.admtel.telephonyserver.requests.Request;
import com.admtel.telephonyserver.requests.ShowStatusRequest;

public class CLI_ShowStatusCommand extends CLI_Command {
	static String command = "show status";
	static int miniumArgs = 2;
	@Override
	Request onParse() {
		return new ShowStatusRequest();
	}

}
