package com.admtel.telephonyserver.core;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.ScriptFactoryDefinition;
import com.admtel.telephonyserver.interfaces.Loadable;
import com.admtel.telephonyserver.interfaces.ScriptFactory;

public class ScriptManager implements DefinitionChangeListener, Loadable{

	Logger log = Logger.getLogger(ScriptManager.class);

	private Map<String, ScriptFactory> scriptFactories = new ConcurrentHashMap<String, ScriptFactory>();

	private ScriptManager() {

	}

	private static class SingletonHolder {
		private final static ScriptManager instance = new ScriptManager();
	}

	public static ScriptManager getInstance() {
		return SingletonHolder.instance;
	}

	public Script createScript(ChannelData channelData) {
		log.debug("Creating script for channelData "+channelData);
		Script script = null;
		for (ScriptFactory sf : scriptFactories.values()) {
			script = sf.createScript(channelData);
			if (script != null) {				
				log.trace(String.format("Script (%s) created for ChannelData (%s)", script, channelData));
				script.onCreate();
				return script;
			}
		}
		return null;
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof ScriptFactoryDefinition) {
			log.trace(String.format("ScriptFactory definition (%s), added", definition));
			ScriptFactoryDefinition sfDefinition = (ScriptFactoryDefinition) definition;
			ScriptFactory sf = SmartClassLoader.createInstance(
					ScriptFactory.class, sfDefinition.getClassName());
			if (sf != null) {
				scriptFactories.put(definition.getId(), sf);
				if (sf instanceof Loadable){
					((Loadable)sf).load();
				}
				log.debug(String.format("ScriptFactory %s added", sf));
			} else {
				log.debug(String.format(
						"Couldn't create Script Factory for definition(%s)",
						definition));
			}

		}

	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		if (definition instanceof ScriptFactoryDefinition) {
			log.trace(String.format("ScriptFactory definition (%s), removed", definition));
			ScriptFactoryDefinition sfDefinition = (ScriptFactoryDefinition) definition;
			scriptFactories.remove(definition.getId());
		}
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		definitionRemoved(oldDefinition);
		definitionAdded(newDefinition);
	}

	@Override
	public void reload() {
		Iterator<ScriptFactory> it = scriptFactories.values().iterator();
		log.trace("Reloading ...");
		while (it.hasNext()){
			ScriptFactory sf = it.next();
			log.trace("Script factor =" + sf +", loadable = "+(sf instanceof Loadable));
			if (sf instanceof Loadable){				
				((Loadable) sf).reload();
			}
		}
		
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}

}
