package com.admtel.telephonyserver.scriptfactories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.ChannelData;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.interfaces.Loadable;
import com.admtel.telephonyserver.interfaces.ScriptFactory;

public class XMLScriptFactory implements ScriptFactory, Loadable {

	static Logger log = Logger.getLogger(XMLScriptFactory.class);

	private XMLConfiguration config;

	private List<ScriptData> scripts = new ArrayList<ScriptData>();

	class ScriptData {
		public String name;
		public String called;
		public String className;
		public Map<String, String> parameters = new HashMap<String, String>();

		// Pattern pattern;

		public ScriptData(String name, String called, String className,
				Map<String, String> parameters) {
			super();
			this.name = name;
			this.called = called;
			this.className = className;
			this.parameters = parameters;
			// this.pattern = Pattern.compile(this.called);
		}

		public String toString() {
			String result = name + ":" + called + ":" + className;
			for (String key : parameters.keySet()) {
				result += "\t" + parameters.get(key);
			}
			return result;
		}

		public boolean matches(String calledNumber) {
			return calledNumber.matches(this.called);
		}

	}

	public XMLScriptFactory() throws ConfigurationException {
		config = new XMLConfiguration("scripts.xml");
	}

	@Override
	public Script createScript(ChannelData channelData) {
		Iterator<ScriptData> it = scripts.iterator();
		while (it.hasNext()) {
			ScriptData scriptData = it.next();
			if (scriptData.matches(channelData.getCalledNumber())) {
				if (scriptData.className != null) {
					Class c = SmartClassLoader.getClass(scriptData.className);
					if (c == null) {
						log.warn("Application " + scriptData.className
								+ ", not found");
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
			}
		}
		return null;
	}

	public Map<String, String> loadParameters(HierarchicalConfiguration section) {
		int coutner = 0;
		Map<String, String> result = new Hashtable<String, String>();

		try {
			int counter = 0;
			while (section.configurationAt(String.format("parameter(%d)",
					counter)) != null) {
				String key = section.getString(String.format(
						"parameter(%d)[@key]", counter));
				String value = section.getString(String.format(
						"parameter(%d)[@value]", counter));
				result.put(key, value);
				counter++;
			}
		} catch (Exception e) {

		}
		return result;

	}

	@Override
	public void load() {
		// TODO, relad logic

		int counter = 0;
		try {
			while (config.configurationAt(String.format("script(%d)", counter)) != null) {

				String name = config.getString(String.format(
						"script(%d)[@name]", counter));
				String called = config.getString(String.format(
						"script(%d).called", counter));
				String className = config.getString(String.format(
						"script(%d).class", counter));
				HierarchicalConfiguration parametersConfig = config
						.configurationAt(String.format("script(%d).parameters",
								counter));
				Map<String, String> parameters = loadParameters(parametersConfig);
				ScriptData sd = new ScriptData(name, called, className,
						parameters);
				scripts.add(sd);
				counter++;
				log.debug("Script definition loaded " + sd);
			}
		} catch (java.lang.IllegalArgumentException ae) {
			log.debug(ae.getMessage());
		} catch (Exception e) {
			log.warn(e.getMessage());
		}

	}

	@Override
	public void reload() {
		load();

	}

}
