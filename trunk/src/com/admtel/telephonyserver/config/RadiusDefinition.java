package com.admtel.telephonyserver.config;

public class RadiusDefinition implements DefinitionInterface {
	String id;
	String address;
	int authPort;
	int acctPort;
	int retryCount;
	int socketTimeout;
	
	Boolean startAccounting;
	Boolean stopAccounting;
	

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

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	@Override
	public String toString() {
		return "RadiusDefinition [acctPort=" + acctPort + ", address="
				+ address + ", authPort=" + authPort + ", id=" + id
				+ ", retryCount=" + retryCount + ", secret=" + secret
				+ ", socketTimeout=" + socketTimeout + ", startAccounting="
				+ startAccounting + ", stopAccounting=" + stopAccounting + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + acctPort;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + authPort;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + retryCount;
		result = prime * result + ((secret == null) ? 0 : secret.hashCode());
		result = prime * result + socketTimeout;
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
		RadiusDefinition other = (RadiusDefinition) obj;
		if (acctPort != other.acctPort)
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (authPort != other.authPort)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (retryCount != other.retryCount)
			return false;
		if (secret == null) {
			if (other.secret != null)
				return false;
		} else if (!secret.equals(other.secret))
			return false;
		if (socketTimeout != other.socketTimeout)
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAuthPort() {
		return authPort;
	}

	public void setAuthPort(int authPort) {
		this.authPort = authPort;
	}

	public int getAcctPort() {
		return acctPort;
	}

	public void setAcctPort(int acctPort) {
		this.acctPort = acctPort;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setId(String id) {
		this.id = id;
	}

	String secret;
	
	@Override
	public String getId() {
		return id;
	}

}
