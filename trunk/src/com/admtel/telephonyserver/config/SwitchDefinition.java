package com.admtel.telephonyserver.config;

import java.util.HashMap;
import java.util.Map;

public class SwitchDefinition implements DefinitionInterface {
	String name;
	String address;
	int port;
	String username;
	String password;
	SwitchType switchType;
	String addressTranslatorClass;
	boolean enabled;
	String signallingIp;

	public String getSignallingIp() {
		return signallingIp;
	}

	public void setSignallingIp(String signallingIp) {
		this.signallingIp = signallingIp;
	}

	Map<String, String> features = new HashMap<String, String>();
	Map<String, String> parameters = new HashMap<String, String>();
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getId() {
		return String.format("%s:%d", address, port);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SwitchType getSwitchType() {
		return switchType;
	}

	public void setSwitchType(SwitchType switchType) {
		this.switchType = switchType;
	}

	public String getAddressTranslatorClass() {
		return addressTranslatorClass;
	}

	public void setAddressTranslatorClass(String addressTranslatorClass) {
		this.addressTranslatorClass = addressTranslatorClass;
	}

	public Map<String, String> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, String> features) {
		this.features = features;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		return "SwitchDefinition [name=" + name + ", address=" + address
				+ ", port=" + port + ", username=" + username + ", password="
				+ password + ", switchType=" + switchType
				+ ", addressTranslatorClass=" + addressTranslatorClass
				+ ", enabled=" + enabled + ", signallingIp=" + signallingIp
				+ ", features=" + features + ", parameters=" + parameters + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime
				* result
				+ ((addressTranslatorClass == null) ? 0
						: addressTranslatorClass.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result
				+ ((features == null) ? 0 : features.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + port;
		result = prime * result
				+ ((signallingIp == null) ? 0 : signallingIp.hashCode());
		result = prime * result
				+ ((switchType == null) ? 0 : switchType.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SwitchDefinition other = (SwitchDefinition) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (addressTranslatorClass == null) {
			if (other.addressTranslatorClass != null)
				return false;
		} else if (!addressTranslatorClass.equals(other.addressTranslatorClass))
			return false;
		if (enabled != other.enabled)
			return false;
		if (features == null) {
			if (other.features != null)
				return false;
		} else if (!features.equals(other.features))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (port != other.port)
			return false;
		if (signallingIp == null) {
			if (other.signallingIp != null)
				return false;
		} else if (!signallingIp.equals(other.signallingIp))
			return false;
		if (switchType != other.switchType)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public boolean isCoreChange(DefinitionInterface definition) {
		if (!(definition instanceof SwitchDefinition)) {
			return false;
		}
		SwitchDefinition switchDefinition = (SwitchDefinition) definition;
		return !switchDefinition.getAddress().equals(getAddress())
				|| !switchDefinition.getPassword().equals(getPassword())
				|| switchDefinition.getPort() != getPort()
				|| switchDefinition.getSwitchType() != getSwitchType()
				|| !switchDefinition.getUsername().equals(getUsername())
				|| switchDefinition.isEnabled() != isEnabled() 
				|| !switchDefinition.getSignallingIp().equals(getSignallingIp());
	}

}
