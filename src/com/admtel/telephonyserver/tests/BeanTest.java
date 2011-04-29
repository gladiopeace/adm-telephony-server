package com.admtel.telephonyserver.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

public class BeanTest {
	// public String address;
	// public int port;

	public static void main(String[] argv) throws Exception {
		// System.out.println("BeanTest initialized with address="+address+", port="+port);
		generateCsvFile("/home/user1/" + new Date() + ".csv");
		generateXMLFile("/home/user1/test.xml");
	}

	private static void generateCsvFile(String sFileName) {
		try {
			FileWriter writer = new FileWriter(sFileName);

			writer.append("ID");
			writer.append(',');
			writer.append("Name");
			writer.append(',');
			writer.append("Old Value");
			writer.append(',');
			writer.append("New Value");
			writer.append('\n');
			
			//write them in a for loop///////////////
			writer.append("been id");
			writer.append(',');
			writer.append("bean Name");
			writer.append(',');
			writer.append("been Old Value");
			writer.append(',');
			writer.append("been New Value");
			writer.append('\n');
			//////////////////////////////////////////
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateXMLFile(String sFileName)throws ParserConfigurationException, TransformerException {
		String root = "cdremitters";
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement(root);
		document.appendChild(rootElement);
		
		//write them in a for loop///////////////
		Element beans = document.createElement("Beans");
		rootElement.appendChild(beans);
		Element id = document.createElement("id");
		id.appendChild(document.createTextNode("bean id"));
		beans.appendChild(id);
		Element name = document.createElement("name");
		name.appendChild(document.createTextNode("bean name"));
		beans.appendChild(name);
		Element oldValue = document.createElement("oldValue");
		oldValue.appendChild(document.createTextNode("bean old value"));
		beans.appendChild(oldValue);
		Element newValue = document.createElement("newValue");
		newValue.appendChild(document.createTextNode("bean new value"));
		beans.appendChild(newValue);
		//////////////////////////////////////////
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(sFileName));
		transformer.transform(source, result);
	}
	
//	private static void generateTextFile(String sFileName) {
//		try{
//		    FileWriter fstream = new FileWriter(sFileName);
//	        BufferedWriter out = new BufferedWriter(fstream);
//		    out.write("Hello Java");
//		    out.close();
//		}catch (Exception e){
//		     System.err.println("Error: " + e.getMessage());
//		}
//	}
	

}