package com.admtel.telephonyserver.tests;

import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.interfaces.ScriptFactory;

public class SmartClassLoaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Script  sf = SmartClassLoader.createInstance(Script.class,
				"com.admtel.telephonyserver.scripts.SimpleTestScript");

	}

}
