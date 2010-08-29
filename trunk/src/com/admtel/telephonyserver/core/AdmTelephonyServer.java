package com.admtel.telephonyserver.core;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.cli.CLI_Connections;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.ServerDefinition;
import com.admtel.telephonyserver.config.SystemConfig;
import com.admtel.telephonyserver.httpserver.HttpServers;
import com.admtel.telephonyserver.prompts.PromptBuilderFactory;
import com.admtel.telephonyserver.radius.RadiusServers;

public class AdmTelephonyServer {

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
		
		
		//TODO remove, for testing only
		log.trace("Prompt builder " + PromptBuilderFactory.getInstance().getPromptBuilder(Locale.ENGLISH));
		
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

		SystemConfig sysConfig = SystemConfig.getInstance();
		
		//static listeners, order is important as conferencemanager might removed objects needed by RadiusServers
		
		EventsManager.getInstance().addEventListener(RadiusServers.getInstance().toString(), RadiusServers.getInstance());
		EventsManager.getInstance().addEventListener("ConferenceManager_Singleton", ConferenceManager.getInstance());
		
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
		sysConfig.addDefinitionChangeListener(PromptBuilderFactory.getInstance());
		//sysConfig.addDefinitionChangeListener(BeansManager.getInstance());
		SystemConfig.getInstance().load();
	}
}
