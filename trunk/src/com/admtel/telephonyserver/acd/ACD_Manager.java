package com.admtel.telephonyserver.acd;

import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;

public class ACD_Manager implements EventListener{
	private ACD_Manager() {

	}

	private static class SingletonHolder {
		private static ACD_Manager instance = new ACD_Manager();
	}

	static public ACD_Manager getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public boolean onEvent(Event event) {
		// TODO Auto-generated method stub
		return false;
	}
}
