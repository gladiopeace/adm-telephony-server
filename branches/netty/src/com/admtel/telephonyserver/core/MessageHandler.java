package com.admtel.telephonyserver.core;

public interface MessageHandler {
	public void putMessage(Object message);
	public void onMessage(Object message);
}
