package com.admtel.telephonyserver.config;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.Switch;
import com.admtel.telephonyserver.core.Switches;

public class SystemConfig {

	private static final String SCRIPTFACTORIES_SCRIPTFACTORY_D_PARAMETERS = "scriptfactories.scriptfactory(%d).parameters";

	private static final String CONFIG_XML = "config.xml";

	private static final String LANGUAGE = "language";

	private static final String PROMPT_BUILDERS_PROMPT_BUILDER_D = "prompt-builders.prompt-builder(%d)";

	private static final String BEANS_BEAN_D_PARAMETERS = "beans.bean(%d).parameters";

	private static final String BEANS_BEAN_D = "beans.bean(%d)";

	private static final String EVENT_LISTENERS_EVENT_LISTENER_D = "event-listeners.event-listener(%d)";

	private static final String SCRIPTFACTORIES_SCRIPTFACTORY_D = "scriptfactories.scriptfactory(%d)";

	private static final String PATH = "path";

	private static final String CLASS = "class";

	private static final String ADM_SERVLET_D = "adm-servlet(%d)";

	private static final String HTTP_SERVERS_HTTP_SERVER_D = "http-servers.http-server(%d)";

	private static final String LOG2 = "log";

	private static final String SOCKET_TIMEOUT = "socket-timeout";

	private static final String RETRY_COUNT = "retry-count";

	private static final String SECRET = "secret";

	private static final String ACCT_PORT = "acct-port";

	private static final String AUTH_PORT = "auth-port";

	private static final String ID = "id";

	private static final String RADIUS_SERVER_D = "radius.server(%d)";

	private static final String SWITCHES_SWITCH_D_FEATURES = "switches.switch(%d).features";

	private static final String SWITCHES_SWITCH_D_PARAMETERS = "switches.switch(%d).parameters";

	private static final String ADDRESSTRANSLATOR = "addresstranslator";

	private static final String PASSWORD = "password";

	private static final String USERNAME = "username";

	private static final String NAME = "name";

	private static final String SWITCHES_SWITCH_D = "switches.switch(%d)";

	private static final String TYPE = "type";

	private static final String PORT = "port";

	private static final String ADDRESS = "address";

	private static final String SWITCH_LISTENERS_SWITCH_LISTENER_D = "switch-listeners.switch-listener(%d)";

	private static final String SERVER_SCRIPT_PATH = "server.script-path";

	private static final String SERVER_BASE_DIRECTORY = "server.base-directory";

	private static final String SERVER_ADDRESS = "server.address";

	private static final String SERVER_MAXTHREADS = "server.maxthreads";

	static Logger log = Logger.getLogger(SystemConfig.class);
	
	XMLConfiguration config;
	String configPath="./";
	
	Map<String, DefinitionInterface> currentDefinitions = new Hashtable<String, DefinitionInterface>();
	Map<String, DefinitionInterface> futureDefinitions = new Hashtable<String, DefinitionInterface>();

	List<DefinitionChangeListener> definitionChangeListeners = new ArrayList<DefinitionChangeListener>();

	public ServerDefinition serverDefinition;

	public void addDefinitionChangeListener(DefinitionChangeListener listener) {
		this.definitionChangeListeners.add(listener);
	}

	public void removeDefinitionChangeListener(DefinitionChangeListener listener) {
		this.definitionChangeListeners.remove(listener);
	}

	private void notifyListenersDeletedDefinition(DefinitionInterface definition) {
		for (DefinitionChangeListener listener : definitionChangeListeners) {
			listener.definitionRemoved(definition);
		}
	}

	private void notifyListenersModifiedDefinition(
			DefinitionInterface oldDefinition, DefinitionInterface newDefinition) {
		for (DefinitionChangeListener listener : definitionChangeListeners) {
			listener.defnitionChanged(oldDefinition, newDefinition);
		}
	}

	private void notifyListenersAddedDefinition(DefinitionInterface definition) {
		for (DefinitionChangeListener listener : definitionChangeListeners) {
			listener.definitionAdded(definition);
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public void loadServerDefinition() {
		ServerDefinition serverDefinition = new ServerDefinition();
		serverDefinition.setMaxThreads(config.getInt(SERVER_MAXTHREADS));
		serverDefinition.setAddress(config.getString(SERVER_ADDRESS));
		serverDefinition.setBaseDirectory(config
				.getString(SERVER_BASE_DIRECTORY));
		serverDefinition.setScriptPath(config.getString(SERVER_SCRIPT_PATH));
		this.serverDefinition = serverDefinition;
	}

	public void loadSwitchListenersDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						SWITCH_LISTENERS_SWITCH_LISTENER_D, counter));
				if (subnode != null) {
					SwitchListenerDefinition definition = new SwitchListenerDefinition();
					definition.setAddress(subnode.getString(ADDRESS));
					definition.setPort(subnode.getInt(PORT));
					definition.setSwitchType(SwitchType.fromString(subnode
							.getString(TYPE)));
					futureDefinitions.put(definition.getId(), definition);
				} else {
					return;
				}
			} catch (Exception e) {
				log.warn(e.getMessage());
				return;
			}
			counter++;

		}
	}

	public void loadSwitchesDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						SWITCHES_SWITCH_D, counter));
				if (subnode != null) {
					SwitchDefinition definition = new SwitchDefinition();
					definition.setName(subnode.getString(NAME));
					definition.setAddress(subnode.getString(ADDRESS));
					definition.setPort(subnode.getInt(PORT));
					definition.setUsername(subnode.getString(USERNAME));
					definition.setPassword(subnode.getString(PASSWORD));
					definition.setSwitchType(SwitchType.fromString(subnode
							.getString(TYPE)));
					definition.setAddressTranslatorClass(subnode
							.getString(ADDRESSTRANSLATOR));
					definition.setEnabled(subnode.getBoolean("enabled", true));
					
					try{
						HierarchicalConfiguration parametersConfig = config
								.configurationAt(String.format(SWITCHES_SWITCH_D_PARAMETERS,
										counter));
						 Map<String,String>parameters = ConfigUtils.loadParameters(parametersConfig);
						 definition.setParameters(parameters);
						}
						catch (Exception e){
							log.warn(e.getMessage());
						}
						try{
							HierarchicalConfiguration parametersConfig = config
									.configurationAt(String.format(SWITCHES_SWITCH_D_FEATURES,
											counter));
							 Map<String,String>features = ConfigUtils.loadFeatures(parametersConfig);
							 definition.setFeatures(features);
							}
							catch (Exception e){
								log.warn(e.getMessage());
							}
					if (definition.isEnabled()){
						futureDefinitions.put(definition.getId(), definition);
					}
				} else {
					return;
				}
			} catch (Exception e) {
				log.warn(e.getMessage());
				return;
			}
			counter++;

		}
	}

	public void loadRadiusDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						RADIUS_SERVER_D, counter));
				if (subnode != null) {
					RadiusDefinition definition = new RadiusDefinition();
					definition.setId(subnode.getString(ID));
					definition.setAddress(subnode.getString(ADDRESS));
					definition.setAuthPort(subnode.getInt(AUTH_PORT, 1812));
					definition.setAcctPort(subnode.getInt(ACCT_PORT, 1813));
					definition.setSecret(subnode.getString(SECRET));
					definition.setRetryCount(subnode.getInt(RETRY_COUNT, 5));
					definition.setSocketTimeout(subnode.getInt(
							SOCKET_TIMEOUT, 5000));
					definition.setLog(subnode.getBoolean(LOG2, false));
					futureDefinitions.put(definition.getId(), definition);
				}
			} catch (Exception e) {
				log.warn(e.getMessage());
				return;
			}
			counter++;
		}
	}

	public void loadHTTPServerDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						HTTP_SERVERS_HTTP_SERVER_D, counter));
				if (subnode != null) {
					HttpServerDefinition definition = new HttpServerDefinition();
					definition.setId(subnode.getString(ID));
					definition.setAddress(subnode.getString(ADDRESS));
					definition.setPort(subnode.getInt(PORT));
					int servletCounter = 0;
					while (true){
						try{
							SubnodeConfiguration servletSubnode = subnode.configurationAt(String.format(ADM_SERVLET_D, servletCounter));
							if (servletSubnode != null){

								AdmServletDefinition servletDefinition = new AdmServletDefinition();
								servletDefinition.setClassName(servletSubnode.getString(CLASS));
								servletDefinition.setPath(servletSubnode.getString(PATH));
								definition.putServletDefinition(servletDefinition.getPath(), servletDefinition);
							}
						}
						catch (Exception e){
							log.warn(e.getMessage());
							break;
						}
						servletCounter ++;
					}
					futureDefinitions.put(definition.getId(), definition);
				}
			} catch (Exception e) {
				log.warn(e.getMessage());
				return;
			}
			counter++;
		}
	}

	public void loadScriptFactoriesDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						SCRIPTFACTORIES_SCRIPTFACTORY_D, counter));
				if (subnode != null) {
					ScriptFactoryDefinition definition = new ScriptFactoryDefinition();
					definition.setClassName(subnode.getString(CLASS));
					try{
						HierarchicalConfiguration parametersConfig = config
								.configurationAt(String.format(SCRIPTFACTORIES_SCRIPTFACTORY_D_PARAMETERS,
										counter));
						 Map<String,String>parameters = ConfigUtils.loadParameters(parametersConfig);
						 definition.setParameters(parameters);
						}
						catch (Exception e){
							log.warn(e.getMessage());
						}

					futureDefinitions.put(definition.getId(), definition);
				} else {
					return;
				}
			} catch (Exception e) {
				log.warn(e.getMessage());
				return;
			}
			counter++;

		}
	}

	public void loadEventListenersDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						EVENT_LISTENERS_EVENT_LISTENER_D, counter));
				if (subnode != null) {
					EventListenerDefinition definition = new EventListenerDefinition();
					definition.setClassName(subnode.getString(CLASS));
					futureDefinitions.put(definition.getId(), definition);
				} else {
					return;
				}
			} catch (Exception e) {
				log.warn(e.getMessage());
				return;
			}
			counter++;

		}
	}
	
	public void loadBeans() {
		log.trace("SystemConfig.loadBeans");
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						BEANS_BEAN_D, counter));
				if (subnode != null) {
					BeanDefinition definition = new BeanDefinition();
					definition.setClassName(subnode.getString(CLASS));
					definition.setId(subnode.getString(ID));
					log.trace(definition);
					try{
						HierarchicalConfiguration parametersConfig = config
								.configurationAt(String.format(BEANS_BEAN_D_PARAMETERS,
										counter));
						 Map<String,String>parameters = ConfigUtils.loadParameters(parametersConfig);
						 definition.setParameters(parameters);
						}
						catch (Exception e){
							log.warn(e.getMessage());
						}

					futureDefinitions.put(definition.getId(), definition);
				} else {
					return;
				}
			} catch (Exception e) {
				log.warn(e.getMessage());
				return;
			}
			counter++;

		}
	}

	public void loadPromptBuildersDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						PROMPT_BUILDERS_PROMPT_BUILDER_D, counter));
				if (subnode != null) {
					PromptBuilderDefinition definition = new PromptBuilderDefinition();
					definition.setClassName(subnode.getString(CLASS));
					definition.setLanguage(subnode.getString(LANGUAGE));
					futureDefinitions.put(definition.getId(), definition);
				} else {
					return;
				}
			} catch (Exception e) {
				log.warn(e.getMessage());
				return;
			}
			counter++;

		}
	}

	public void load() {
		log.trace("Loading System configuration ...");
		futureDefinitions.clear();
		loadServerDefinition();
		loadSwitchListenersDefinition();
		loadScriptFactoriesDefinition();
		loadSwitchesDefinition();
		loadScriptFactoriesDefinition();
		loadRadiusDefinition();
		loadEventListenersDefinition();
		loadHTTPServerDefinition();
		loadPromptBuildersDefinition();
		loadBeans();
		// Compare the 2 lists of definitions
		// Check what was added
		log.trace("Starting definition notification");
		Map<String, DefinitionInterface> addedDefinitions = new Hashtable<String, DefinitionInterface>(
				futureDefinitions);
		addedDefinitions.keySet().removeAll(currentDefinitions.keySet());
		for (DefinitionInterface definition : addedDefinitions.values()) {			
			try{
				log.trace("emit notifyListenerAddedDefinition for ("+definition+")");
				this.notifyListenersAddedDefinition(definition);
			}
			catch (Exception e){
				log.error("emit notifyListenerAddedDefinition for ("+definition+")"+ "-- FAILED ("+ e+")");
			}
		}
		// Check for what was removed
		Map<String, DefinitionInterface> removedDefinitions = new Hashtable<String, DefinitionInterface>(
				currentDefinitions);
		removedDefinitions.keySet().removeAll(futureDefinitions.keySet());
		for (DefinitionInterface definition : removedDefinitions.values()) {
			try{
				log.trace("emit notifyListenerDeletedDefinition for ("+definition+")");
				this.notifyListenersDeletedDefinition(definition);
			}
			catch (Exception e){
				log.error("emit notifyListenerDeletedDefinition for ("+definition+")"+ "-- FAILED ("+ e+")");
			}
		}

		// Check for what was changed
		Map<String, DefinitionInterface> retainedDefinitions = new Hashtable<String, DefinitionInterface>(
				currentDefinitions);
		retainedDefinitions.keySet().retainAll(futureDefinitions.keySet());
		for (String definitionId : retainedDefinitions.keySet()) {
			DefinitionInterface oldDefinition = currentDefinitions
					.get(definitionId);
			DefinitionInterface newDefinition = futureDefinitions
					.get(definitionId);
			if (!oldDefinition.equals(newDefinition)) {
				try{
					log.trace("emit notifyListenersModifiedDefinition for ("+newDefinition+")");
					this.notifyListenersModifiedDefinition(oldDefinition,
							newDefinition);
				}
				catch (Exception e){
					log.trace("emit notifyListenersModifiedDefinition for ("+newDefinition+") -- FAILED : "+e);
				}
			}
		}
		// After sending all the notifications, set the current definitions to
		// the future definitions
		currentDefinitions = new Hashtable<String, DefinitionInterface>(
				futureDefinitions);
		log.debug("Loaded System Configuration ...");
	}

	private SystemConfig() {
		try {
			config = new XMLConfiguration(configPath+CONFIG_XML);
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			// config.setExpressionEngine(new XPathExpressionEngine());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setConfigPath(String configPath){
		this.configPath=configPath;
	}
	private static class SingletonHolder {
		private final static SystemConfig instance = new SystemConfig();
	}

	public static SystemConfig getInstance() {
		return SingletonHolder.instance;
	}

}
