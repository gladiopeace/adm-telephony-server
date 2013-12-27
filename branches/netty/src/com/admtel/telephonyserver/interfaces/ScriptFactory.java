package com.admtel.telephonyserver.interfaces;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.ChannelData;
import com.admtel.telephonyserver.core.Script;



public interface ScriptFactory {
	public Script createScript (Channel channel);
}
