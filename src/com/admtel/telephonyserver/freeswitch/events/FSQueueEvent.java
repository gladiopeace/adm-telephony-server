package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSQueueEvent extends FSChannelEvent {

	public enum Action{none, push, predial, postdial, consumer_start, caller_pop, consumer_pop, bridgeconsumerstart, bridgecallerstart, bridgeconsumerstop, consumer_stop};
	Action action = Action.none;	
	public FSQueueEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.Queue;
		try
		{
			String actionStr = (String) values.get("FIFO-Action");
			actionStr = actionStr.replaceAll("-", "");
			action = Action.valueOf(actionStr);
		}
		catch (Exception e){
			action = Action.none;
		}
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	public String getQueueName(){
		return values.get("FIFO-Name");
	}
	public Action getAction(){		
		return action;
	}

	public String getPeerChannel(){
		return values.get("Other-Leg-Unique-ID");
	}
	public String getStatus() {
		return values.get("variable_fifo_status");
	}

}
