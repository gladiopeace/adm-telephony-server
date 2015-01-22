package com.admtel.telephonyserver.scriptfactories;

import java.util.HashMap;
import java.util.Map;

public class ScriptData {
	public String name;
	public String called;
	public String context;
	public String className;
	public Map<String, String> parameters = new HashMap<String, String>();

	// Pattern pattern;

	public ScriptData(String name, String called, String className, String context,
			Map<String, String> parameters) {
		super();
		this.name = name;
		this.called = called;
		this.className = className;
		this.parameters = parameters;
		this.context = context;
		// this.pattern = Pattern.compile(this.called);
	}

	public String toString() {
		String result = name + ":" + called + ":" + className+":"+context;
		for (String key : parameters.keySet()) {
			result += "\t" + key+"="+ parameters.get(key);
		}
		return result;
	}

	public boolean matches(String calledNumber) {
		return calledNumber.matches(this.called);
	}

}