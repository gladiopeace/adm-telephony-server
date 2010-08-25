package com.admtel.telephonyserver.core;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilerConfiguration;

import com.admtel.telephonyserver.utils.AdmUtils;


import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;

public class SmartClassLoader {
	
	static Logger log = Logger.getLogger(SmartClassLoader.class);
	
	static GroovyClassLoader groovyClassLoader;
	static GroovyScriptEngine groovyScriptEngine;
	static ClassLoader classLoader;
	static {
		classLoader = SmartClassLoader.class.getClassLoader(); 
		groovyClassLoader = new GroovyClassLoader(classLoader);
		try {

			String[] roots = AdmTelephonyServer.getInstance().getDefinition().getScriptPath().split(";");			
			groovyScriptEngine = new GroovyScriptEngine(roots);
			log.trace("Starting GroovyScriptEngine with root = ");
			for (int i=0;i<roots.length;i++){
				log.trace("root["+i+"]="+roots[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.fatal(e.toString());
		}
	}
	public static Class getClass(String className){
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
			log.fatal(AdmUtils.getStackTrace(e));
		}
		return null;
	}	
	
	public static <T> T createInstance(Class<T> classType, String className){
		Class c = getClass(className);
		if (c == null) return null;
		if (classType.isAssignableFrom(c)){
			try {
				return (T)c.newInstance();
			} catch (InstantiationException e) {
				log.fatal(AdmUtils.getStackTrace(e));
			} catch (IllegalAccessException e) {
				log.fatal(AdmUtils.getStackTrace(e));
			}
		}
		return null;
		
	}
}
