package com.admtel.telephonyserver.interfaces;

import com.admtel.telephonyserver.events.Event;

public interface EventListener {
	public boolean onEvent (Event event);
}
