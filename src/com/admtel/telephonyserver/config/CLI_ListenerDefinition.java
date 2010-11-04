package com.admtel.telephonyserver.config;

public class CLI_ListenerDefinition implements DefinitionInterface{
	
	String address;
	int port;
	String username;
	String password;
	
	@Override
	public String getId() {
		return String.format("%s:%d", address, port);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + port;
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
		if (!(obj instanceof CLI_ListenerDefinition)) {
			return false;
		}
		CLI_ListenerDefinition other = (CLI_ListenerDefinition) obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
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
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return (address != null ? "address=" + address + " \\n, " : "")
				+ (password != null ? "password=" + password + " \\n, " : "")
				+ "port=" + port + " \\n, "
				+ (username != null ? "username=" + username : "");
	}

	@Override
	public boolean isCoreChange(DefinitionInterface definition) {
		// TODO Auto-generated method stub
		return false;
	}

}
