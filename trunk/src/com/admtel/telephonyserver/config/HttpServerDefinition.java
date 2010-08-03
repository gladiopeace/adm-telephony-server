package com.admtel.telephonyserver.config;

import java.util.HashMap;
import java.util.Map;

public class HttpServerDefinition implements DefinitionInterface {

	String id;
	String address;
	int port;
	String admServletClass;
	
	public String getAdmServletClass() {
		return admServletClass;
	}

	public void setAdmServletClass(String admServletClass) {
		this.admServletClass = admServletClass;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((admServletClass == null) ? 0 : admServletClass.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + port;
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
		HttpServerDefinition other = (HttpServerDefinition) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (admServletClass == null) {
			if (other.admServletClass != null)
				return false;
		} else if (!admServletClass.equals(other.admServletClass))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (port != other.port)
			return false;
		return true;
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

}
