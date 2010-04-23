package com.admtel.telephonyserver.config;

public class SwitchDefinition implements DefinitionInterface{
	String id;
	String address;
	int port;
	String username;
	String password;
	SwitchType switchType;
	String addressTranslatorClass;
	
	@Override
	public String getId() {
		return id;
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
	public void setId(String id) {
		this.id = id;
	}

	public String getAddressTranslatorClass() {
		return addressTranslatorClass;
	}
	public void setAddressTranslatorClass(String addressTranslatorClass) {
		this.addressTranslatorClass = addressTranslatorClass;
	}
	public String toString(){
		return super.toString()+":"+this.id+":"+this.address+":"+this.port;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + port;
		result = prime * result
				+ ((switchType == null) ? 0 : switchType.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		if (!(obj instanceof SwitchDefinition)) {
			return false;
		}
		SwitchDefinition other = (SwitchDefinition) obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
			return false;
		}
		if (addressTranslatorClass == null) {
			if (other.addressTranslatorClass != null) {
				return false;
			}
		} else if (!addressTranslatorClass.equals(other.addressTranslatorClass)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
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
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}
	
}
