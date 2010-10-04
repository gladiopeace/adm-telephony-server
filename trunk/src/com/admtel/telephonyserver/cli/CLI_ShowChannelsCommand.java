package com.admtel.telephonyserver.cli;

import com.admtel.telephonyserver.requests.Request;
import com.admtel.telephonyserver.requests.ShowChannelsRequest;

public class CLI_ShowChannelsCommand extends CLI_Command {
	static{
		command = "show channels";
		minimumArgs = 2;
	}
	@Override
	Request onParse() {
		ShowChannelsRequest result = null;
		if (cmds.length == 2){
			result = new ShowChannelsRequest("all");
		}
		else{
			result = new ShowChannelsRequest(cmds[2]);
		}
		return result;
	}

}
