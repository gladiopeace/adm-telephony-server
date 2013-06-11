package com.admtel.telephonyserver.misc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class VariableMap extends HashMap<String, String> {
	public void addVariable(String varName, String value){
		put(varName, value);
	}
	public String getVariable(String varName){
		return get(varName);
	}
	public void addVariables(Map<String, String>vars){
		if (vars != null){
			keySet().addAll(vars.keySet());
		}
	}
	public void addDelimitedVars(String varStr, String delimiter){
		addDelimitedVars(varStr, "=", delimiter);
	}
	public void addDelimitedVars(String varStr, String keyValueSeparator, String delimiter){
		if (varStr != null) {
			String values[] = varStr.split(delimiter);
			for (int i = 0; i < values.length; i++) {
				String[] key_value = values[i].split(keyValueSeparator);
				if (key_value.length == 2) {
					put(key_value[0], key_value[1]);
				}
			}
		}		
	}
	public String getDelimitedVars(String keyValueSeparator, String delimiter){
		StringBuffer sb = new StringBuffer();
		Set<java.util.Map.Entry<String, String>> s = this.entrySet();
		Iterator<java.util.Map.Entry<String, String>> it = s.iterator();
		while (it.hasNext()){
			java.util.Map.Entry<String,String> entry = it.next();
			sb.append(entry.getKey());
			sb.append(keyValueSeparator);
			sb.append(entry.getValue());
			if (it.hasNext()){
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}
}
