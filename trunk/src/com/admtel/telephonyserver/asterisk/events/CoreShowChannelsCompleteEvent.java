package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

public class CoreShowChannelsCompleteEvent extends ASTEvent {

	public String eventList;
	public Integer listItems;
	public CoreShowChannelsCompleteEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.CoreShowChannelsComplete;
		eventList = values.get("EventList");
		String listItemsStr = values.get("ListItems");
		try {
			listItems = Integer.parseInt(listItemsStr);
		}
		catch (Exception e) {
			listItems = 0;
		}
	}

}
