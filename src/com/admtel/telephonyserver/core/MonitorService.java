package com.admtel.telephonyserver.core;

import org.apache.log4j.Logger;

public class MonitorService implements Runnable{
	
	public static final long TIMEOUT = 5000L;
	
	static Logger log = Logger.getLogger(MonitorService.class);
	
	Thread thread;
	boolean running;
	
	private MonitorService() {
		
	}	
	
	private static class SingletonHolder{
		private static final MonitorService instance = new MonitorService();
	}
	
	public void start() {
		if (running) return;
		running = true;
		thread = new Thread (this);	
		thread.start();
	}
	
	public static MonitorService getInstance() {
		return SingletonHolder.instance;
	}

	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (running) {
			try {
				
				log.trace("Monitor service fired");
				Thread.sleep(TIMEOUT);
			} catch (InterruptedException e) {
				log.fatal(e);
			}
		}
	}
}
