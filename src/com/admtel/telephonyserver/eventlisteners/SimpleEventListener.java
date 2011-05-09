package com.admtel.telephonyserver.eventlisteners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
				bw.write (eventDate);
				bw.write(',');
				bw.write (eventName);
				bw.newLine();
				bw.flush();
				bw.close();
			}else{
				FileWriter writer = new FileWriter(fileName);
				writer.append("Event Date");
				writer.append(',');
				writer.append("Event Name");
				writer.append('\n');
				
				writer.append(eventDate);
				writer.append(',');
				writer.append(eventName);
				writer.append('\n');
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateXMLFile(String fileName, String eventName, String eventDate)throws ParserConfigurationException, TransformerException, SAXException, IOException {
		if ((new File(fileName)).exists()){
			File file = new File(fileName);
			 
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			Element rootElement = document.getDocumentElement();
			
			Element beans = document.createElement("beans");
			rootElement.appendChild(beans);
			Element name = document.createElement("eventDate");
			name.appendChild(document.createTextNode(eventDate));
			beans.appendChild(name);
			Element id = document.createElement("eventName");
			id.appendChild(document.createTextNode(eventName));
			beans.appendChild(id);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);
		}else{
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("cdremitters");
			document.appendChild(rootElement);
			
			Element beans = document.createElement("beans");
			rootElement.appendChild(beans);
			Element name = document.createElement("eventDate");
			name.appendChild(document.createTextNode(eventDate));
			beans.appendChild(name);
			Element id = document.createElement("eventName");
			id.appendChild(document.createTextNode(eventName));
			beans.appendChild(id);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);
		}
	}
	
	public void createDirectory(){
		try{
			File 	CdrEmittersFile = new File("/tmp/CdrEmitters");
			File eventListnerFile = new File("/tmp/CdrEmitters/eventListner");
			File CSVFile = new File("/tmp/CdrEmitters/eventListner/CSVFiles");
			File XMLFile = new File("/tmp/CdrEmitters/eventListner/XMLFiles");
			CdrEmittersFile.mkdir();
			eventListnerFile.mkdir();
			CSVFile.mkdir();
			XMLFile.mkdir();
		}catch(Exception e){
			
		}
	}
	
	public void generateFiles(String eventName){
		try {
			Date date = new Date();
			createDirectory();
			generateCsvFile("/tmp/CdrEmitters/eventListner/CSVFiles/" + new DateTime().getDayOfMonth() + "-" + new DateTime().getMonthOfYear() + "-" + new DateTime().getYear() + ".csv", eventName, date.toString());
			generateXMLFile("/tmp/CdrEmitters/eventListner/XMLFiles/" + new DateTime().getDayOfMonth() + "-" + new DateTime().getMonthOfYear() + "-" + new DateTime().getYear() + ".xml", eventName, date.toString());
		} catch (ParserConfigurationException e) {
			log.trace("Parser Configuration Exception");
		} catch (TransformerException e) {
			log.trace("Transformer Exception");
		} catch (SAXException e) {
			log.trace("SAX Exception");
		} catch (IOException e) {
			log.trace("IO Exception");
		} catch (Exception e) {
			log.trace("Exception");
		}
	}
	
}