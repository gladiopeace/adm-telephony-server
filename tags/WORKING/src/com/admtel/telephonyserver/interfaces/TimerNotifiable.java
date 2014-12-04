package com.admtel.telephonyserver.interfaces;

public interface TimerNotifiable {
	public boolean onTimer(Object data); //Return true to stop the timer
}
