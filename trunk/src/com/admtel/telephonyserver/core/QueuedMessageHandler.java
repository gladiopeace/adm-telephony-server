package com.admtel.telephonyserver.core;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;


public abstract class QueuedMessageHandler implements Runnable, MessageHandler {

	Logger log = Logger.getLogger(QueuedMessageHandler.class);
	
	Queue<Object> messages = new LinkedList<Object>();

   @Override
   public void putMessage(Object message){
			synchronized (messages){
				boolean startThread = messages.isEmpty();
				messages.offer(message);
				if (startThread){
					AdmThreadExecutor.getInstance().execute(this);
				}
			}
	}
	
	@Override
	public void run() {
		while (true){
			Object message = null;
			synchronized(messages){
				message = messages.peek();
				if (message == null){
					return;
				}
			}			
			onMessage(message);
			synchronized(messages){
				messages.poll();
				if (messages.isEmpty()){
					return;
				}
			}
		}
	}
}
