package com.admtel.telephonyserver.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AdmUtils {

	final static Pattern IP_PATTERN = Pattern
			.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	static public DateTimeFormatter RADIUS_TIME_FORMAT = DateTimeFormat
			.forPattern("HH:mm:ss.SSS z EEE MMM dd yyyy");
	static Logger log = Logger.getLogger(AdmUtils.class);
	static DateTimeFormatter SIMPLE_DATE_TIME_FORMAT = DateTimeFormat
			.forPattern("yyyy-MM-dd HH:mm:ss");
	static DateTimeFormatter SIMPLE_DATE_TIME_AT_MIDNIGHT_FORMAT = DateTimeFormat
			.forPattern("yyyy-MM-dd 00:00:00");
	static DateTimeFormatter SIMPLE_DATE_TIME_JUST_BEFORE_MIDNIGHT_FORMAT = DateTimeFormat
			.forPattern("yyyy-MM-dd 23:59:59");

	public static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}

	public static boolean validateIP(String iPaddress) {
		return IP_PATTERN.matcher(iPaddress).matches();
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

	public static String addWithDelimiter(String str, String toAdd,
			String delimiter) {
		String result;
		if (str.isEmpty()) {
			result = toAdd;
		} else {
			result = str + delimiter + toAdd;
		}
		return result;
	}

	// FOR JODA parding with z is not working
	/*
	 * static public Date getDateFromRadiusStr (String pRadiusStr) { if
	 * (pRadiusStr == null) return null;
	 * 
	 * Date result = null; try{ DateTime dt =
	 * RADIUS_TIME_FORMAT.parseDateTime(pRadiusStr); result = dt.toDate(); }
	 * catch (Exception e){ log.fatal(e.getMessage()); } return result; }
	 */
	static public Date getDateFromRadiusStr(String pRadiusStr) {
		if (pRadiusStr == null || pRadiusStr.length() == 0)
			return null;
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS z EEE MMM dd yyyy");
		Date result = null;
		try {
			result = df.parse(pRadiusStr);
		} catch (Exception e) {
			log.fatal("Error parsing " + pRadiusStr + " - " + e.getMessage());
		}
		return result;
	}
	
	static public String dateToRadiusStr(DateTime date){
		return RADIUS_TIME_FORMAT.print(date);
	}

	static public String dateToStr(DateTime dt) {
		return SIMPLE_DATE_TIME_FORMAT.print(dt);
	}

	static public DateTimeZone getTimezone(String timezone) {
		try {
			DateTimeZone dateTimeZone = DateTimeZone.forID(timezone);
			return dateTimeZone;
		} catch (IllegalArgumentException e) {
			return DateTimeZone.getDefault();
		}

	}

	static public String toSimpleDateString(DateTime dateTime) {
		return dateTime.toString("YYYY-MM-dd");
	}

	static public DateTime getDateAtMidnight(Date date, String timeZone) {
		DateTime d1 = new DateTime(date, AdmUtils.getTimezone(timeZone));
		LocalTime l = new LocalTime(0, 0, 0);// midnight
		d1 = d1.withFields(l);
		d1 = new DateTime(d1, DateTimeZone.getDefault());
		return d1;
	}

	static public void main(String[] args) {
		/*DateTimeZone timezone = AdmUtils.getTimezone("America/Los_Angeles");
		System.out.println(toSimpleDateString(new DateTime()));
		System.out.println(timezone);
		System.out
				.println(getDateAtMidnight(new Date(), "America/Los_Angeles"));*/
		System.out.println(dateToRadiusStr(new DateTime()));
	}

	static public String getHostAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}
}
