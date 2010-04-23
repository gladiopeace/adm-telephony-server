package com.admtel.telephonyserver.core;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.cli.CLI_Connections;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.SystemConfig;

public class AdmTelephonyServer implements DefinitionChangeListener {

	static Logger log = Logger.getLogger(AdmTelephonyServer.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.debug("Adm Telephony Server started ...");
		AdmTelephonyServer server = new AdmTelephonyServer();
		server.start();
		while (true) {
			try {
				Thread.sleep(10000);
				//SystemConfig.getInstance().load();
			} catch (Exception e) {

			}
			log.debug("Server running...");
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
		
		SystemConfig.getInstance().load();
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		log.debug("Definition : " + definition + ", added");
	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		log.debug("Definition : " + definition + ", removed");
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		log.debug("Definition : " + oldDefinition + " changed to "
				+ newDefinition);
	}

}
