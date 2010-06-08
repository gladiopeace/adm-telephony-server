package com.admtel.telephonyserver.config;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

public class SystemConfig {

	static Logger log = Logger.getLogger(SystemConfig.class);

	XMLConfiguration config;
	Map<String, DefinitionInterface> currentDefinitions = new Hashtable<String, DefinitionInterface>();
	Map<String, DefinitionInterface> futureDefinitions = new Hashtable<String, DefinitionInterface>();

	List<DefinitionChangeListener> definitionChangeListeners = new ArrayList<DefinitionChangeListener>();

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
		serverDefinition.setMaxThreads(config.getInt("server.maxthreads"));
		serverDefinition.setAddress(config.getString("server.address"));
		futureDefinitions.put(serverDefinition.getId(), serverDefinition);
	}
	public void loadRegistrarDefinition(){
		RegistrarDefinition registrarDefinition = new RegistrarDefinition();
		registrarDefinition.setClassName(config.getString("registrar.class", "com.admtel.telephonyserver.registrar.SimpleRegistrar"));
		registrarDefinition.setEnabled(config.getBoolean("registrar.enabled", true));
		futureDefinitions.put(registrarDefinition.getId(), registrarDefinition);
	}
	public void loadSwitchListenersDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						"switch-listeners.switch-listener(%d)", counter));
				if (subnode != null) {
					SwitchListenerDefinition definition = new SwitchListenerDefinition();
					definition.setAddress(subnode.getString("address"));
					definition.setPort(subnode.getInt("port"));
					definition.setSwitchType(SwitchType.fromString(subnode
							.getString("type")));
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
						"switches.switch(%d)", counter));
				if (subnode != null) {
					SwitchDefinition definition = new SwitchDefinition();
					definition.setId(subnode.getString("id"));
					definition.setAddress(subnode.getString("address"));
					definition.setPort(subnode.getInt("port"));
					definition.setUsername(subnode.getString("username"));
					definition.setPassword(subnode.getString("password"));
					definition.setSwitchType(SwitchType.fromString(subnode
							.getString("type")));
					definition.setAddressTranslatorClass(subnode.getString("addresstranslator"));
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

	public void loadRadiusDefinition(){
		int counter=0;
		SubnodeConfiguration subnode;
		while (true){
			try{
				subnode = config.configurationAt(String.format("radius.server(%d)", counter));
				if (subnode != null){
					RadiusDefinition definition = new RadiusDefinition();
					definition.setId(subnode.getString("id"));
					definition.setAddress(subnode.getString("address"));
					definition.setAuthPort(subnode.getInt("auth-port", 1812));
					definition.setAcctPort(subnode.getInt("acct-port", 1813));
					definition.setSecret(subnode.getString("secret"));
					definition.setRetryCount(subnode.getInt("retry-count",5));
					definition.setSocketTimeout(subnode.getInt("socket-timeout", 5000));
					futureDefinitions.put(definition.getId(), definition);
				}
			}
			catch (Exception e){
				log.warn(e.getMessage());
				return;
			}
			counter++;
		}
	}
	public void loadCLI_ListenersDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						"cli-listeners.cli-listener(%d)", counter));
				if (subnode != null) {
					CLI_ListenerDefinition definition = new CLI_ListenerDefinition();
					definition.setAddress(subnode.getString("address"));
					definition.setPort(subnode.getInt("port"));
					definition.setUsername(subnode.getString("username"));
					definition.setPassword(subnode.getString("password"));
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

	public void loadScriptFactoriesDefinition() {
		int counter = 0;
		SubnodeConfiguration subnode;
		while (true) {
			try {
				subnode = config.configurationAt(String.format(
						"scriptfactories.scriptfactory(%d)", counter));
				if (subnode != null) {
					ScriptFactoryDefinition definition = new ScriptFactoryDefinition();
					definition.setClassName(subnode.getString("class"));
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
		log.debug("Loading System configuration ...");
		futureDefinitions.clear();
		loadServerDefinition();
		loadSwitchListenersDefinition();
		loadCLI_ListenersDefinition();
		loadScriptFactoriesDefinition();
		loadSwitchesDefinition();
		loadScriptFactoriesDefinition();
		loadRadiusDefinition();
		loadRegistrarDefinition();
		// Dump the loaded configurations

		for (DefinitionInterface definition : futureDefinitions.values()) {
			log.trace(String.format(definition.toString()));
		}

		// Compare the 2 lists of definitions
		// Check what was added
		Map<String, DefinitionInterface> addedDefinitions = new Hashtable<String, DefinitionInterface>(
				futureDefinitions);
		addedDefinitions.keySet().removeAll(currentDefinitions.keySet());
		for (DefinitionInterface definition : addedDefinitions.values()) {
			this.notifyListenersAddedDefinition(definition);
		}

		// Check for what was removed
		Map<String, DefinitionInterface> removedDefinitions = new Hashtable<String, DefinitionInterface>(
				currentDefinitions);
		removedDefinitions.keySet().removeAll(futureDefinitions.keySet());
		for (DefinitionInterface definition : removedDefinitions.values()) {
			this.notifyListenersDeletedDefinition(definition);
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
				this.notifyListenersModifiedDefinition(oldDefinition,
						newDefinition);
			}
		}
		// After sending all the notifications, set the current definitions to
		// the future definitions
		currentDefinitions = new Hashtable<String, DefinitionInterface>(
				futureDefinitions);
		for (DefinitionInterface definition : currentDefinitions.values()) {
			log.trace("Current definitions "
					+ String.format(definition.toString()));
		}

		log.debug("Loaded System Configuration ...");
	}

	private SystemConfig() {
		try {
			config = new XMLConfiguration("config.xml");
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			// config.setExpressionEngine(new XPathExpressionEngine());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class SingletonHolder {
		private final static SystemConfig instance = new SystemConfig();
	}

	public static SystemConfig getInstance() {
		return SingletonHolder.instance;
	}

}
