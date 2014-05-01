package com.admtel.telephonyserver.commands;


import java.util.Collection;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.ParseResult;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.Switch;
import com.admtel.telephonyserver.core.Switches;

public class CommandShowChannelsExecutor implements ICommandExecutor {
	IoSession session;
	public CommandShowChannelsExecutor(IoSession session){
		this.session = session;
		
	}
	@Override
	public void execute(ParseResult pr) throws ExecutionException {
		List<Channel> channels = Switches.getInstance().getAllChannels();
		StringBuilder sb = new StringBuilder();
		sb.append("Channels ");
		sb.append(channels.size());
		sb.append("\n>\n");
		for (Channel channel:channels) {
			sb.append(channel.toString());
			sb.append("\n");
		}
		sb.append("\n>");
		session.write(sb.toString());
	}
}
