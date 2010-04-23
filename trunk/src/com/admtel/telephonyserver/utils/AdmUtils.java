package com.admtel.telephonyserver.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AdmUtils {

	final static Pattern IP_PATTERN = Pattern
			.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	public static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}

	public static boolean validateIP(String iPaddress) {
		return IP_PATTERN.matcher(iPaddress).matches();
	}

	public static void main(String[] args) {
		String ip = "172.16.140.1";
		System.out.println(String.format("Address %s is %s", ip,
				(validateIP(ip) ? "Valid" : "Invalid")));
	}

	public static Map<String, String> parseVars(String varStr, String separator) {
		Map<String, String> result = new HashMap<String, String>();
		if (varStr != null) {
			String values[] = varStr.split(separator);
			for (int i = 0; i < values.length; i++) {
				String[] key_value = values[i].split("=");
				if (key_value.length == 2) {
					result.put(key_value[0], key_value[1]);
				}
			}
		}
		return result;
	}

	public static int strToLong(String str, int defVal) {
		try {
			return Integer.valueOf(str);
		} catch (Exception e) {

		}
		return defVal;
	}
	public static String addWithDelimiter(String str, String toAdd, String delimiter){
		String result;
		if (str.isEmpty()){
			result = toAdd;
		}
		else{
			result = str+delimiter+toAdd;
		}
		return result;
	}
}
