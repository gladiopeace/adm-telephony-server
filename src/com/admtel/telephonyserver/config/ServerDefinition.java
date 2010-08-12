package com.admtel.telephonyserver.config;

public class ServerDefinition implements DefinitionInterface {

	int maxThreads = 10;
	String address = "127.0.0.1";
	String baseDirectory = "/usr/local/adm";
	Boolean startAccounting = true;
	Boolean stopAccounting = true;
	int interimUpdate =10;

	
	public Boolean getStartAccounting() {
		return startAccounting;
	}
	public void setStartAccounting(Boolean startAccounting) {
		this.startAccounting = startAccounting;
	}
	public Boolean getStopAccounting() {
		return stopAccounting;
	}
	public void setStopAccounting(Boolean stopAccounting) {
		this.stopAccounting = stopAccounting;
	}
	public int getInterimUpdate() {
		return interimUpdate;
	}
	public void setInterimUpdate(int interimUpdate) {
		this.interimUpdate = interimUpdate;
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
		result = prime * result
				+ ((baseDirectory == null) ? 0 : baseDirectory.hashCode());
		result = prime * result + interimUpdate;
		result = prime * result + maxThreads;
		result = prime * result
				+ ((startAccounting == null) ? 0 : startAccounting.hashCode());
		result = prime * result
				+ ((stopAccounting == null) ? 0 : stopAccounting.hashCode());
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
		if (interimUpdate != other.interimUpdate)
			return false;
		if (maxThreads != other.maxThreads)
			return false;
		if (startAccounting == null) {
			if (other.startAccounting != null)
				return false;
		} else if (!startAccounting.equals(other.startAccounting))
			return false;
		if (stopAccounting == null) {
			if (other.stopAccounting != null)
				return false;
		} else if (!stopAccounting.equals(other.stopAccounting))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ServerDefinition [address=" + address + ", baseDirectory="
				+ baseDirectory + ", interimUpdate=" + interimUpdate
				+ ", maxThreads=" + maxThreads + ", startAccounting="
				+ startAccounting + ", stopAccounting=" + stopAccounting + "]";
	}
}
