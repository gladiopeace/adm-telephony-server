package com.admtel.telephonyserver.events.asterisk;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.freeswitch.FSEvent;

public class ASTEvent{
	private static Map<String, Constructor> EVENTS_MAP = new HashMap<String, Constructor>();
	private static Logger log = Logger.getLogger(ASTEvent.class);
	
	static{
		try{
			EVENTS_MAP.put("Newchannel", ASTNewChannelEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("Newstate", ASTNewStateEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("Hangup", ASTHangupEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("AGIExec", ASTAgiExecEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("DTMF", ASTDtmfEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("OriginateResponse", ASTOriginateResponseEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("Dial", ASTDialEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("Bridge", ASTBridgeEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("AsyncAGI", ASTAsyncAgiEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("MeetmeJoin", ASTMeetmeJoinEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("MeetmeLeave", ASTMeetmeLeaveEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("MeetmeTalking", ASTMeetmeTalkingEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("PeerStatus", ASTPeerStatusEvent.class.getConstructor(String.class, Map.class));
		}
		catch (Exception e) {
			log.fatal(e.getMessage());
		}
	}
	
	static public ASTEvent buildEvent(String switchId, String eventStr){
		Map<String, String> map = new HashMap<String, String>();
		
		ASTEvent result = null;
		String[] items = eventStr.split("\r\n"); 
	
		for (int i = 0; i < items.length; i++) {
			String[] values = items[i].split(": ");
			if (values.length==2){
			map.put(values[0].trim(), values[1].trim());
			}			
		}
		if (map.get("agi_request") != null){
			result = new ASTAsyncAgiEvent(switchId, map);
		}
		else if (map.containsKey("Event")){
			String eventName = map.get("Event");
			result = createFromConstructor(switchId, eventName, map);
		}
		else if (map.containsKey("Response")){
				result = new ASTResponseEvent(switchId, map);
		}
		return result;
	}
	static ASTEvent createFromConstructor(String switchId, String eventName, Map<String, String> map){
		if (eventName != null) {
			Constructor ctor = EVENTS_MAP.get(eventName);
			if (ctor == null) {
				return null;
			}

			if (ctor != null) {
				try {
					return (ASTEvent)ctor.newInstance(switchId, map);
				} catch (Exception ex) {
					log.fatal(ex.toString());
				}
			}
		}
		return null;
	}
	protected Map<String, String> values = new HashMap<String,String>();
	
	public enum EventType{
		Response, AsyncAgi, NewChannel, NewState, Dtmf, Hangup, AgiExec, OriginateResponse, Dial, Bridge, MeetmeJoin, MeetmeTalking, MeetmeLeave, PeerStatus};
	
	protected EventType eventType;
	private String switchId;

	public EventType getEventType() {
		return eventType;
	}
	
	public ASTEvent (String switchId, Map<String, String> values){
		this.values = values;
		this.switchId = switchId;
	}
	
	public String getSwitchId(){
		return this.switchId;
	}
	public String getValue(String key){
		return values.get(key);
	}
	public String toString(){
		return String.format("%s:%s:%s", this.getClass().getSimpleName(), eventType, this.switchId);
	}

}
