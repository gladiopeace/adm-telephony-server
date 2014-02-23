package com.admtel.telephonyserver.config;

import java.util.HashMap;
import java.util.Map;

public class ScriptFactoryDefinition implements DefinitionInterface{
	String className;
	
	Map<String, String>parameters = new HashMap<String, String>();
	
	@Override
	public String getId() {
		return className;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
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
		if (!(obj instanceof ScriptFactoryDefinition)) {
			return false;
		}
		ScriptFactoryDefinition other = (ScriptFactoryDefinition) obj;
		if (className == null) {
			if (other.className != null) {
				return false;
			}
		} else if (!className.equals(other.className)) {
			return false;
		}
		return true;
	}
	public String toString(){
		return super.toString()+":"+this.className;
	}

	@Override
	public boolean isCoreChange(DefinitionInterface definition) {
		// TODO Auto-generated method stub
		return false;
	}

}
