package com.admtel.telephonyserver.tests;

import com.admtel.telephonyserver.config.SystemConfig;

public class ConfigTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SystemConfig.getInstance().load();
	}

}
