package com.admtel.telephonyserver.config;

public class ServerDefinition implements DefinitionInterface {

	int maxThreads = 10;
	String address = "127.0.0.1";
	String baseDirectory = "/usr/local/adm";
	String scriptPath=".;./scripts";
	
	public String getScriptPath() {
		return scriptPath;
	}
	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}
	public String getAddress() {
		return address;
	}
	public String getBaseDirectory() {
		return baseDirectory;
	}
	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String getId() {
		return "Server";
	}
	public int getMaxThreads() {
		return maxThreads;
	}
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((baseDirectory == null) ? 0 : baseDirectory.hashCode());
		result = prime * result + maxThreads;
		result = prime * result + ((scriptPath == null) ? 0 : scriptPath.hashCode());
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
		ServerDefinition other = (ServerDefinition) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (baseDirectory == null) {
			if (other.baseDirectory != null)
				return false;
		} else if (!baseDirectory.equals(other.baseDirectory))
			return false;
		if (maxThreads != other.maxThreads)
			return false;
		if (scriptPath == null) {
			if (other.scriptPath != null)
				return false;
		} else if (!scriptPath.equals(other.scriptPath))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ServerDefinition [maxThreads=" + maxThreads + ", "
				+ (address != null ? "address=" + address + ", " : "")
				+ (baseDirectory != null ? "baseDirectory=" + baseDirectory + ", " : "")
				+ (scriptPath != null ? "scriptPath=" + scriptPath : "") + "]";
	}
	@Override
	public boolean isCoreChange(DefinitionInterface definition) {
		// TODO Auto-generated method stub
		return false;
	}
}
