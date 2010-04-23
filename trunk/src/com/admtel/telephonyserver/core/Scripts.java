package com.admtel.telephonyserver.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Scripts {
	
	Map<String, Script> scripts = new HashMap<String, Script>();
	Map<String, Script> synchronizedScripts = Collections.synchronizedMap(scripts);
	
	private Scripts(){
		
	}
	//Thread safe
	private static class SingletonHolder{
		private static final Scripts instance = new Scripts();
	}
	
	public static Scripts getInstance(){
		return SingletonHolder.instance;		
	}
	public void add(Script script){
		if (script == null) return;
		synchronizedScripts.put(script.getId(), script);
	}
	public void remove(Script script){
		if (script == null) return;
		synchronizedScripts.remove(script.getId());
	}
	public Collection<Script> getAll(){
		return scripts.values();
	}
}
