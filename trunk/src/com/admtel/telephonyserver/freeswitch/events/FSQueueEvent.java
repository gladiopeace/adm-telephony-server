package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

public class FSQueueEvent extends FSChannelEvent {

	public enum Action{None, Push, Abort};
	public FSQueueEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.Queue;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	public String getQueueName(){
		return values.get("FIFO-Name");
	}
	public Action getAction(){
		String action = values.get("FIFO-Action");
		Action result = Action.None;
		if (action != null){
			if (action.equals("push")){
				result = Action.Push;
			}
			else if (action.equals("abort")){
				result = Action.Abort;
			}
		}
		return result;
	}

	public String getStatus() {
		return values.get("variable_fifo_status");
	}

}
