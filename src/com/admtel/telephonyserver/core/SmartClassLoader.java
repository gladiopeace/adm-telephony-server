package com.admtel.telephonyserver.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.config.SystemConfig;
import com.admtel.telephonyserver.misc.AdmGroovyScriptEngine;

import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;

public class SmartClassLoader {
	
	static Logger log = Logger.getLogger(SmartClassLoader.class);
	
	GroovyScriptEngine groovyScriptEngine;
	ClassLoader classLoader;
	
	private static URL[] createRoots(String[] urls) throws MalformedURLException {
        if (urls == null) return null;
        URL[] roots = new URL[urls.length];
        for (int i = 0; i < roots.length; i++) {
            if (urls[i].indexOf("://") != -1) {
                roots[i] = new URL(urls[i]);
            } else {
                roots[i] = new File(urls[i]).toURI().toURL();
            }
        }
        return roots;
    }
	
	private SmartClassLoader(){
		classLoader = SmartClassLoader.class.getClassLoader(); 
		try {

			String[] roots = SystemConfig.getInstance().serverDefinition.getScriptPath().split(";");
			
			groovyScriptEngine = new GroovyScriptEngine(roots, classLoader);
			URL[] urls = createRoots(roots);
			log.trace("Starting GroovyScriptEngine with root = ");
			for (int i=0;i<roots.length;i++){
				log.trace("root["+i+"]="+roots[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.fatal(e.toString());
		}		
	}
	
	private static class SingletonHolder {
		private static SmartClassLoader _instance = new SmartClassLoader();
	}
	
	public static SmartClassLoader getInstance(){
		return SingletonHolder._instance;
	}
	
	public Class getClassI(String className){
		if (className == null) return null;
		try{
		if (className.endsWith(".groovy")){
			//Class c = groovyClassLoader.parseClass(new File(className));			
			Class c = groovyScriptEngine.loadScriptByName(className);		
			
			return c;  
		}		
		else{
			try{
				return classLoader.loadClass(className);
			}
			catch (ClassNotFoundException e){
				log.warn("Class "+className +", not found--"+e.toString());
				return null;
			}
		}
		}
		catch (Exception e){
			log.fatal(e.getMessage(), e);
		}
		return null;
	}	
	
	public <T> T createInstanceI(Class<T> classType, String className){
		if (className.startsWith("#")){
			return (T)BeansManager.getInstance().getBean(className.substring(1));
		}
		Class c = getClassI(className);
		if (c == null) {
			log.fatal("Could not find class : " + className);
			return null;
		}
		if (classType.isAssignableFrom(c)){
			try {
				return (T)c.newInstance();
			} catch (Exception e) {
				log.fatal(e.getMessage(), e);
			}
		}
		else{
			log.fatal("class : " + className +", is not assignable from "+classType);
		}
		return null;
		
	}
	
	
	static public <T> T createInstance(Class<T> classType, String className){
		log.trace("Creating instance of class " + className);
		T result = SmartClassLoader.getInstance().createInstanceI(classType, className);
		if (result == null){
			log.warn("Couldn't create class " + className);
		}
		else{
			log.trace("Created class returned " + result);
		}
		return result;
	}
	
}


//package com.admtel.telephonyserver.core;
//
//import java.io.IOException;
//import java.security.AccessController;
//import java.security.PrivilegedAction;
//
//import org.apache.log4j.Logger;
//
//import com.admtel.telephonyserver.config.ServerDefinition;
//import com.admtel.telephonyserver.config.SystemConfig;
//import groovy.util.GroovyScriptEngine;
//
//public class SmartClassLoader {
//
//	static Logger log = Logger.getLogger(SmartClassLoader.class);
//
//	GroovyScriptEngine groovyScriptEngine;
//	static ClassLoader classLoader;
//
//	private SmartClassLoader() {
//		classLoader = SmartClassLoader.class.getClassLoader();
//		try {
//			String[] roots = { "." };
//			ServerDefinition serverDefinition = SystemConfig.getInstance().serverDefinition;
//			if (serverDefinition != null) {
//				roots = SystemConfig.getInstance().serverDefinition
//						.getScriptPath().split(";");
//			}
//
//			groovyScriptEngine = new GroovyScriptEngine(roots);
//			log.trace("Starting GroovyScriptEngine with root = ");
//			for (int i = 0; i < roots.length; i++) {
//				log.trace("root[" + i + "]=" + roots[i]);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			log.fatal(e.toString());
//		}
//	}
//
//	private static class SingletonHolder {
//		private static SmartClassLoader _instance = new SmartClassLoader();
//	}
//
//	public static SmartClassLoader getInstance() {
//		return SingletonHolder._instance;
//	}
//
//	public Class getClassI(String className) {
//		if (className == null)
//			return null;
//		try {
//			if (className.endsWith(".groovy")) {
//				// Class c = groovyClassLoader.parseClass(new File(className));
//				Class c = groovyScriptEngine.loadScriptByName(className);
//
//				return c;
//			} else {
//				try {
//					return classLoader.loadClass(className);
//				} catch (ClassNotFoundException e) {
//					log.warn("Class " + className + ", not found--"
//							+ e.toString());
//					return null;
//				}
//			}
//		} catch (Exception e) {
//			log.fatal(e.getMessage(), e);
//		}
//		return null;
//	}
//
//	public <T> T createInstanceI(Class<T> classType, String className) {
//		if (className.startsWith("#")) {
//			return (T) BeansManager.getInstance().getBean(
//					className.substring(1));
//		}
//		Class c = getClassI(className);
//		if (c == null)
//			return null;
//		if (classType.isAssignableFrom(c)) {
//			try {
//				return (T) c.newInstance();
//			} catch (Exception e) {
//				log.fatal(e.getMessage(), e);
//			}
//		}
//		return null;
//
//	}
//
//	 static public <T> T createInstance(final Class<T> classType, final String
//	 className) {
//	
//	 log.trace("Creating instance of class " + className);
//	 AccessController.doPrivileged(new PrivilegedAction<Object>() {
//	 public Object run() {
//	 classLoader = null;
//	 T result = null;
//	 try {
//	 classLoader = Thread.currentThread().getContextClassLoader();
//	 result = SmartClassLoader.getInstance().createInstanceI(classType,
//	 className);
//	 if (result == null) {
//	 log.warn("Couldn't create class " + className);
//	 } else {
//	 log.trace("Created class returned " + result);
//	 }
//						
//	 } catch (SecurityException ex) {
//	 }
//	 //return classLoader;
//	 return result;
//	 }
//	 });
//	 return null;
//	
//			
//	 }
//}
