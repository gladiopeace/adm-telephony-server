package com.admtel.telephonyserver.scriptfactories;

import java.util.Map;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.ChannelData;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.interfaces.ScriptFactory;
import com.admtel.telephonyserver.scripts.SimpleTestScript;

public class SimpleScriptFactory implements ScriptFactory {

	@Override
	public Script createScript (Channel channel){
		// TODO Auto-generated method stub
		return new SimpleTestScript();
	}

	@Override
	public void setParameters(Map<String, String> parameters) {
		// TODO Auto-generated method stub
		
	}
}
