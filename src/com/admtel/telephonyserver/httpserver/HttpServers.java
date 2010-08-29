package com.admtel.telephonyserver.httpserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.apache.mina.filter.executor.UnorderedThreadPoolExecutor;

import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.HttpServerDefinition;

public class HttpServers implements DefinitionChangeListener {

	Map<String, HttpServer> idMap = new HashMap<String, HttpServer>();
	static Logger log = Logger.getLogger(HttpServers.class);
	Executor executor = new UnorderedThreadPoolExecutor();
	
	private HttpServers(){
		
	}
	private static class SingletonHolder {
		private static HttpServers instance = new HttpServers();
	}
	
	public static HttpServers getInstance(){
		return SingletonHolder.instance;
	}
	
	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof HttpServerDefinition){
			HttpServerDefinition httpServerDefinition = (HttpServerDefinition) definition;
			HttpServer httpServer = new HttpServer (httpServerDefinition);
			put(httpServer);
			httpServer.start(executor);
		}

	}

	private void put(HttpServer httpServer) {
		if(httpServer != null){
			synchronized(this){
				idMap.put(httpServer.getId(), httpServer);
			}
		}
		
	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		// TODO Auto-generated method stub

	}
}
