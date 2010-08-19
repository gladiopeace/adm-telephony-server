package com.admtel.telephonyserver.tests;

import java.util.Locale;

public class LocaleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		Locale mLocal = new Locale("arw","SA");
		System.out.println(mLocal);
		Locale lLocal = new Locale("EN","us");
		System.out.println(lLocal);
	}

}
