package com.admtel.telephonyserver.core;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.DisconnectCode;
import com.admtel.telephonyserver.misc.VariableMap;

public class API_Manager {
	static Logger log = Logger.getLogger(API_Manager.class);
	private API_Manager(){
		
	}
	
	private static class SingletonHolder{
		private static final API_Manager instance = new API_Manager();
	}
	
	public static API_Manager getInstance(){
		return SingletonHolder.instance;
	}
	
	public Result originate (String destination, String script, VariableMap data, int timeout){
		Result result = Result.Ok;
		Switch s = Switches.getInstance().getRandom(); //TODO a better way to get a switch based on the originate parameters
		if (s==null){
			log.warn("Couldn't allocate a switch for originate");
			return Result.InvalidResource;
		}		
		result = s.originate(destination, timeout, "", "", script, data);
		return result;
	}
	public Result hangup (String channelId){
		Result result = Result.Ok;
		Channel c = Switches.getInstance().getChannelById(channelId);
		if (c==null){
			log.warn(String.format("hangup : %s, channel not found", channelId));
			return Result.ChannelInvalid;
		}
		c.hangup(DisconnectCode.Normal);
		return result;
	}
	
	public Result conferenceMute (String channelId, boolean mute){
		Result result = Result.Ok;
		Channel c = Switches.getInstance().getChannelById(channelId);
		if (c==null) return Result.ChannelInvalid;
		result = c.conferenceMute(mute);
		return result;
	}
	public Result conferenceDeaf (String channelId, boolean deaf){
		Result result = Result.Ok;
		Channel c = Switches.getInstance().getChannelById(channelId);
		if (c==null) return Result.ChannelInvalid;
		result = c.conferenceDeaf(deaf);
		return result;		
	}
	public Result dial(String channelId, String destination, long timeout){
		Result result = Result.Ok;
		Channel c = Switches.getInstance().getChannelById(channelId);
		if (c==null) return Result.ChannelInvalid;
		result = c.dial(destination, timeout);
		return result;
	}
	public Result conferenceJoin(String channelId, String conferenceId, boolean moderator,
			boolean startMuted, boolean startDeaf){
		Channel c = Switches.getInstance().getChannelById(channelId);
		if (c==null) return Result.ChannelInvalid;
		return c.joinConference(conferenceId, moderator, startMuted, startDeaf);
	}
}
