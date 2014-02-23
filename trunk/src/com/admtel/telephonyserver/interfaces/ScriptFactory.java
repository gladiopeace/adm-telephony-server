package com.admtel.telephonyserver.interfaces;

import java.util.Map;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.ChannelData;
import com.admtel.telephonyserver.core.Script;



public interface ScriptFactory {
	public Script createScript (Channel channel);
	public void setParameters(Map<String, String> parameters);
}
