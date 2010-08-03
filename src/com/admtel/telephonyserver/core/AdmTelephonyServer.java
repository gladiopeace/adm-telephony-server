package com.admtel.telephonyserver.core;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.cli.CLI_Connections;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.ServerDefinition;
import com.admtel.telephonyserver.config.SystemConfig;
import com.admtel.telephonyserver.httpserver.HttpServers;
import com.admtel.telephonyserver.radius.RadiusServers;

public class AdmTelephonyServer implements DefinitionChangeListener {

	static Logger log = Logger.getLogger(AdmTelephonyServer.class);

	private ServerDefinition definition;
	
	private static class SingletonHolder {
		private static AdmTelephonyServer instance = new AdmTelephonyServer();
	}
	
	public static AdmTelephonyServer getInstance(){
		return SingletonHolder.instance;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.debug("Adm Telephony Server started ...");
		getInstance().start();
		while (true) {
			try {
				Thread.sleep(30000);
				//SystemConfig.getInstance().load();
			} catch (Exception e) {

			}
			//log.debug("Server running...");
		}
	}

	private void start() {
		SystemConfig.getInstance().addDefinitionChangeListener(this);
		
		SystemConfig sysConfig = SystemConfig.getInstance();
		
		sysConfig.addDefinitionChangeListener(
				Switches.getInstance());
		sysConfig.addDefinitionChangeListener(
				SwitchListeners.getInstance());
		sysConfig.addDefinitionChangeListener(ScriptManager.getInstance());
		
		sysConfig.addDefinitionChangeListener(CLI_Connections.getInstance());
		sysConfig.addDefinitionChangeListener(RadiusServers.getInstance());
		sysConfig.addDefinitionChangeListener(Registrar.getInstance());
		sysConfig.addDefinitionChangeListener(EventsManager.getInstance());
		sysConfig.addDefinitionChangeListener(HttpServers.getInstance());
		
		SystemConfig.getInstance().load();
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		log.debug("Definition : " + definition + ", added");
		if (definition instanceof ServerDefinition){
			this.definition = (ServerDefinition)definition;
		}
	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		log.debug("Definition : " + definition + ", removed");
		//TODO update server definition
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		log.debug("Definition : " + oldDefinition + " changed to "
				+ newDefinition);
		//TODO, update server definition
	}
	public ServerDefinition getDefinition() {
		return definition;
	}
	public void setDefinition(ServerDefinition definition) {
		this.definition = definition;
	}

}
