package com.admtel.telephonyserver.tests;

import java.io.IOException;

import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.interfaces.ScriptFactory;

public class SmartClassLoaderTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		for (int i=0;i<10000;i++){
		Script  sf = SmartClassLoader.createInstance(Script.class,
				"EnumTest.java");
		}
		
		System.in.read();

	}

}
