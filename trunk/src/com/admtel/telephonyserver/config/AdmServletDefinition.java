package com.admtel.telephonyserver.config;

import java.util.HashMap;
import java.util.Map;

public class AdmServletDefinition {
	String path;
	String className;
	Map<String, String>parameters = new HashMap<String, String>();
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Map<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
}
