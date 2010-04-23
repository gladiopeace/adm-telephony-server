package com.admtel.telephonyserver.tests;

import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.interfaces.ScriptFactory;

public class SmartClassLoaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ScriptFactory sf = SmartClassLoader.createInstance(ScriptFactory.class,
				"com.admtel.telephonyserver.scripts.SimpleTestScript");

	}

}
