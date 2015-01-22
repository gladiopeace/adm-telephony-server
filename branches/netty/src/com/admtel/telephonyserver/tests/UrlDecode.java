package com.admtel.telephonyserver.tests;

import com.admtel.telephonyserver.utils.CodecsUtils;

public class UrlDecode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(CodecsUtils.urlDecode("sofia%3A%3Aregister"));

	}

}
