package com.admtel.telephonyserver.cli;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.config.CLI_ListenerDefinition;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.utils.AdmUtils;

public class CLI_Connections implements DefinitionChangeListener{
	
	Map<String, CLI_Connection> cliConnections = new HashMap<String, CLI_Connection>();
	static Logger log = Logger.getLogger(CLI_Connections.class);
	private CLI_Connections(){
		
	}
	private static class SingletonHolder{
		private final static CLI_Connections instance = new CLI_Connections();
	}
	
	public static CLI_Connections getInstance(){
		return SingletonHolder.instance;
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof CLI_ListenerDefinition){
			CLI_Connection c = new CLI_Connection((CLI_ListenerDefinition)definition);
			cliConnections.put(definition.getId(), c);
			try {
				c.start();
			} catch (IOException e) {
				log.fatal(AdmUtils.getStackTrace(e));
			}
		}
		
	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		// TODO Auto-generated method stub
		
	}
}
