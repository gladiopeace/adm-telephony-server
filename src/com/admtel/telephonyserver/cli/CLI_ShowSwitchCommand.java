package com.admtel.telephonyserver.cli;

import com.admtel.telephonyserver.requests.Request;
import com.admtel.telephonyserver.requests.ShowSwitchRequest;

public class CLI_ShowSwitchCommand extends CLI_Command {
	static String command = "show switch";
	static int miniumArgs = 2;
	
	@Override
	Request onParse() {
		return new ShowSwitchRequest();
	}

}
