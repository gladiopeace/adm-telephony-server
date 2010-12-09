package com.admtel.telephonyserver.httpserver;

import java.util.Collections;
import java.util.Map;

abstract public class AdmServlet {
	Map<String, String> parameters = Collections.EMPTY_MAP;
	
	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getParameter(String key){
		if (parameters == null) return null;
		return parameters.get(key);
	}
	abstract public void process (HttpRequestMessage request, HttpResponseMessage response);
}
