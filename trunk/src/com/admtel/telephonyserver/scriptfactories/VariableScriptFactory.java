package com.admtel.telephonyserver.scriptfactories;

import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.ChannelData;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.interfaces.ScriptFactory;

public class VariableScriptFactory implements ScriptFactory {

	static Logger log = Logger.getLogger(VariableScriptFactory.class);

	@Override
	public Script createScript(Channel channel) {
		log.debug(String.format("Creating script for (%s)", channel.getChannelData()
				.toString()));
		String scriptName = channel.getChannelData().get("script");
		if (scriptName != null) {
			try {
				Script script = (Script) SmartClassLoader.createInstance(Script.class, scriptName);
				if (script != null) {
					log.debug(String.format(
							"Created script for (%s) - script (%s)",
							channel.getChannelData().toString(), script));
				}
				return script;
			} catch (Exception e) {
				log.fatal(e.getMessage(), e);
			} 
		}
		return null;
	}

	@Override
	public void setParameters(Map<String, String> parameters) {
		// TODO Auto-generated method stub
		
	}

}
