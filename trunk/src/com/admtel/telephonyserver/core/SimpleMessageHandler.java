package com.admtel.telephonyserver.core;

public abstract class SimpleMessageHandler implements MessageHandler {

	private class MessageProcessor implements Runnable{
		Object message = null;
		MessageProcessor(Object message){
			this.message = message;
		}

		@Override
		public void run() {
			SimpleMessageHandler.this.onMessage(message);
			
		}
	}
	@Override
	final public void putMessage(Object message) {
		AdmThreadExecutor.getInstance().execute(new MessageProcessor(message));

	}
}
