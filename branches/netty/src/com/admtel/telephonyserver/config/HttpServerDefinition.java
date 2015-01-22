package com.admtel.telephonyserver.config;

import java.util.HashMap;
import java.util.Map;

import com.admtel.telephonyserver.httpserver.AdmServlet;

public class HttpServerDefinition implements DefinitionInterface {

	String id;
	String address;
	int port;

	
	Map<String, AdmServletDefinition> admServlets = new HashMap<String, AdmServletDefinition>();	

	public AdmServletDefinition getServletDefinition(String path){
		return admServlets.get(path);
	}
	public void putServletDefinition(String path, AdmServletDefinition servletDefinition){
		admServlets.put(path, servletDefinition);
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

	@Override
	public boolean isCoreChange(DefinitionInterface definition) {
		// TODO Auto-generated method stub
		return false;
	}

}
