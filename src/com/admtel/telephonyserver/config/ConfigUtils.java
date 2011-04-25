package com.admtel.telephonyserver.config;

import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;

public class ConfigUtils {
	static public Map<String, String> loadParameters(HierarchicalConfiguration section) {
		Map<String, String> result = new Hashtable<String, String>();

		try {
			int counter = 0;
			while (section.configurationAt(String.format("parameter(%d)",
					counter)) != null) {
				String key = section.getString(String.format(
						"parameter(%d)[@name]", counter));
				String value = section.getString(String.format(
						"parameter(%d)[@value]", counter));
				result.put(key, value);
				counter++;
			}
		} catch (Exception e) {

		}
		return result;

	}
	static public Map<String, String> loadFeatures(HierarchicalConfiguration section) {
	
		Map<String, String> result = new Hashtable<String, String>();

		try {
			int counter = 0;
			while (section.configurationAt(String.format("feature(%d)",
					counter)) != null) {
				String key = section.getString(String.format(
						"feature(%d)[@name]", counter));
				String value = section.getString(String.format(
						"feature(%d)[@value]", counter));
				result.put(key, value);
				counter++;
			}
		} catch (Exception e) {

		}
		return result;

	}
}
