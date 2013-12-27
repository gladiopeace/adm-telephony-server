package com.admtel.telephonyserver.tests;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class JodaTimeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DateTime connectedTime = new DateTime();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new Duration (connectedTime, new DateTime()).getStandardSeconds());

	}

}
