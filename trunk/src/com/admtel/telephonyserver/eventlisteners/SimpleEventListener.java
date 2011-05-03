package com.admtel.telephonyserver.eventlisteners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;

public class SimpleEventListener implements EventListener{
	static Logger log = Logger.getLogger(SimpleEventListener.class);
	
	@Override
	public boolean onEvent(Event event) {
		log.trace(event);
		return true;
	}

	public void generateCsvFile(String fileName, String eventName, String eventDate) {
		try {
			
			if ((new File(fileName)).exists()){
				BufferedWriter bw = new BufferedWriter (new FileWriter (fileName, true));
				bw.write (eventName);
				bw.write(',');
				bw.write (eventDate);
				bw.newLine();
				bw.flush();
				bw.close();
			}else{
				FileWriter writer = new FileWriter(fileName);
				writer.append("Event Name");
				writer.append(',');
				writer.append("Event Date");
				writer.append('\n');
				
				writer.append(eventName);
				writer.append(',');
				writer.append(eventDate);
				writer.append('\n');
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateXMLFile(String fileName, String eventName, String eventDate)throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement("cdremitters");
		document.appendChild(rootElement);
		
		//write them in a for loop///////////////
		Element beans = document.createElement("beans");
		rootElement.appendChild(beans);
		Element id = document.createElement("eventName");
		id.appendChild(document.createTextNode(eventName));
		beans.appendChild(id);
		Element name = document.createElement("eventDate");
		name.appendChild(document.createTextNode(eventDate));
		beans.appendChild(name);
		//////////////////////////////////////////
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(fileName));
		transformer.transform(source, result);
	}
	
}