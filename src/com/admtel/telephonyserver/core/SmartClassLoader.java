package com.admtel.telephonyserver.core;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilerConfiguration;

import com.admtel.telephonyserver.config.SystemConfig;
import com.admtel.telephonyserver.utils.AdmUtils;


import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;

public class SmartClassLoader {
	
	static Logger log = Logger.getLogger(SmartClassLoader.class);
	
	GroovyClassLoader groovyClassLoader;
	GroovyScriptEngine groovyScriptEngine;
	ClassLoader classLoader;
	
	private SmartClassLoader(){
		classLoader = SmartClassLoader.class.getClassLoader(); 
		groovyClassLoader = new GroovyClassLoader(classLoader);
		try {

			String[] roots = SystemConfig.getInstance().serverDefinition.getScriptPath().split(";");
			
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
		Class c = getClassI(className);
		if (c == null) return null;
		if (classType.isAssignableFrom(c)){
			try {
				return (T)c.newInstance();
			} catch (Exception e) {
				log.fatal(e.getMessage(), e);
			}
		}
		return null;
		
	}
	
	
	static public <T> T createInstance(Class<T> classType, String className){
		return SmartClassLoader.getInstance().createInstanceI(classType, className);
	}
	
}
