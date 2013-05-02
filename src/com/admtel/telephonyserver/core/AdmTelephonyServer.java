package com.admtel.telephonyserver.core;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
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
import com.admtel.telephonyserver.remote.EventDto;
import com.admtel.telephonyserver.remote.SimpleMessageDto;
import com.admtel.telephonyserver.requests.HangupRequest;
import com.admtel.telephonyserver.requests.Request;
import com.admtel.telephonyserver.requests.SwitchRequest;

public class AdmTelephonyServer {

	private static final int PORT = 9999;	

	static Logger log = Logger.getLogger(AdmTelephonyServer.class);

	private ServerDefinition definition;

	private static class SingletonHolder {
		private static AdmTelephonyServer instance = new AdmTelephonyServer();
	}

	public static AdmTelephonyServer getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * @param args
	 */
	static private boolean isOnlyInstance(){
		
		ServerSocket socket = null;
		try{
			socket = new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }));
		}
		catch (Exception e){
			return false;
		}
		if (socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				log.warn(e);
			}
			socket = null;
		}
		return true;
	}
	
	public static void main(String[] args) {

		if (!isOnlyInstance()){
			log.error("Server already running ");
			System.exit(1);
		}
		try {
			log.debug("Adm Telephony Server started ...");
			getInstance().start();

			while (true) {
				try {
					Thread.sleep(30000);
					// SystemConfig.getInstance().load();
				} catch (Exception e) {

				}
				// log.debug("Server running...");
			}
		} catch (Exception e) {
			log.error(e); 
			System.exit(1);
		}
	}

	public EventDto processRequest(Request request) {
		log.trace(String.format("Received request %s", request));
		EventDto result = null;
		switch (request.getType()) {
		case ReloadRequest:
			log.trace("Reloading configuration ...");
			SystemConfig.getInstance().load();
			ScriptManager.getInstance().reload();
			break;
		case ShowStatusRequest:
			result = new SimpleMessageDto(AdmThreadExecutor.getInstance()
					.getStatus());
			break;
		case ShowSwitchRequest:
			result = new SimpleMessageDto(Switches.getInstance()
					.toReadableString());
			break;
		}
		// TODO better request processing
		Switches.getInstance().processRequest(request);
		return result;
	}

	private void start() {

		SystemConfig sysConfig = SystemConfig.getInstance();

		// static listeners, order is important as conferencemanager might
		// removed objects needed by RadiusServers
		log.trace("AdmTelephonyServer.start ****************");
		EventsManager.getInstance().addEventListener(
				RadiusServers.getInstance().toString(),
				RadiusServers.getInstance());
		EventsManager.getInstance().addEventListener(
				"ConferenceManager_Singleton", ConferenceManager.getInstance());
		EventsManager.getInstance().addEventListener("Switches_Singleton",
				Switches.getInstance());

		sysConfig.addDefinitionChangeListener(Switches.getInstance());
		sysConfig.addDefinitionChangeListener(SwitchListeners.getInstance());
		sysConfig.addDefinitionChangeListener(ScriptManager.getInstance());

		sysConfig.addDefinitionChangeListener(CLI_Connections.getInstance());
		sysConfig.addDefinitionChangeListener(RadiusServers.getInstance());
		sysConfig.addDefinitionChangeListener(Registrar.getInstance());
		sysConfig.addDefinitionChangeListener(EventsManager.getInstance());
		sysConfig.addDefinitionChangeListener(HttpServers.getInstance());
		sysConfig.addDefinitionChangeListener(PromptBuilderFactory
				.getInstance());
		
		log.trace("################################################");
		sysConfig.addDefinitionChangeListener(BeansManager.getInstance());
		SystemConfig.getInstance().load();
		log.trace("loaded system config ***************");
		definition = sysConfig.serverDefinition;
		BeansManager.getInstance().init();
	}
}
