package com.admtel.telephonyserver.config;

public class SwitchListenerDefinition implements DefinitionInterface{
	String address;
	int port;
	SwitchType switchType;
	public String getAddress() {
		return address;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + port;
		result = prime * result
				+ ((switchType == null) ? 0 : switchType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SwitchListenerDefinition)) {
			return false;
		}
		SwitchListenerDefinition other = (SwitchListenerDefinition) obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
			return false;
		}
		if (port != other.port) {
			return false;
		}
		if (switchType == null) {
			if (other.switchType != null) {
				return false;
			}
		} else if (!switchType.equals(other.switchType)) {
			return false;
		}
		return true;
	}
	public void setAddress(String ip) {
		this.address = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public SwitchType getSwitchType() {
		return switchType;
	}
	public void setSwitchType(SwitchType switchType) {
		this.switchType = switchType;
	}
	@Override
	public String getId() {
		return String.format("%s:%d", address, port);
	}
	public String toString(){
		return super.toString()+":"+this.address+":"+this.port+":"+this.switchType;
	}
	@Override
	public boolean isCoreChange(DefinitionInterface definition) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
