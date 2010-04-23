package com.admtel.telephonyserver.scriptfactories;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.ChannelData;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.interfaces.ScriptFactory;

public class VariableScriptFactory implements ScriptFactory {

	static Logger log = Logger.getLogger(VariableScriptFactory.class);

	@Override
	public Script createScript(ChannelData channelData) {
		log.debug(String.format("Creating script for (%s)", channelData
				.toString()));
		String scriptName = channelData.get("script");
		if (scriptName != null) {
			Class c = SmartClassLoader.getClass(scriptName);
			if (c == null) {
				log.warn("Script " + scriptName + ", not found");
				return null;
			}
			try {
				Script script = (Script) c.newInstance();
				if (script != null) {
					log.debug(String.format(
							"Created script for (%s) - script (%s)",
							channelData.toString(), script));
				}
				return script;
			} catch (InstantiationException e) {
				log.fatal(e.getMessage());
			} catch (IllegalAccessException e) {
				log.fatal(e.getMessage());
			}
		}
		return null;
	}

}
