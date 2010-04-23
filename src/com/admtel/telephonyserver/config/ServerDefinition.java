package com.admtel.telephonyserver.config;

public class ServerDefinition implements DefinitionInterface {

	int maxThreads = 10;
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
		result = prime * result + maxThreads;
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
		if (maxThreads != other.maxThreads)
			return false;
		return true;
	}
	
	public String toString(){
		return super.toString()+":"+this.maxThreads;
	}
}
