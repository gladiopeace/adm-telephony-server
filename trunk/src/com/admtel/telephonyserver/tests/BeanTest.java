package com.admtel.telephonyserver.tests;

import java.util.Date;

import com.admtel.telephonyserver.eventlisteners.SimpleEventListener;
import org.joda.time.DateTime;

public class BeanTest {
	// public static String address;
	 //public static int port;

	public static void main(String[] argv) throws Exception {
		//System.out.println("BeanTest initialized with address="+address+", port="+port);
		SimpleEventListener simpleEventListenerInstance = new SimpleEventListener();
		Date date = new Date();
		simpleEventListenerInstance.generateCsvFile("/home/user1/ZZZ/" + new DateTime().getDayOfMonth() + "-" + new DateTime().getMonthOfYear() + "-" + new DateTime().getYear() + ".csv", "Hang Up", date.toString());
		simpleEventListenerInstance.generateXMLFile("/home/user1/ZZZ/" + new DateTime().getDayOfMonth() + "-" + new DateTime().getMonthOfYear() + "-" + new DateTime().getYear() + ".xml", "Hang Up", date.toString());
	}


}