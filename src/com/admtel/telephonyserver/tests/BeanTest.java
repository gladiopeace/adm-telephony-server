package com.admtel.telephonyserver.tests;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.admtel.telephonyserver.eventlisteners.SimpleEventListener;
import org.joda.time.DateTime;
import org.xml.sax.SAXException;

public class BeanTest {
	// public static String address;
	 //public static int port;

	public static void main(String[] argv) throws ParserConfigurationException, TransformerException, SAXException, IOException {
		//System.out.println("BeanTest initialized with address="+address+", port="+port);
		//for (int i=0; i<1000; i++){
			SimpleEventListener simpleEventListenerInstance = new SimpleEventListener();
			simpleEventListenerInstance.generateFiles("HangUp");
		//}
	}
}