package com.admtel.telephonyserver.freeswitch.events;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.SigProtocol;
import com.admtel.telephonyserver.utils.CodecsUtils;

public class FSEvent {

	static Logger log = Logger.getLogger(FSEvent.class);

	static Map<String, Constructor> EVENTS_MAP = new HashMap<String, Constructor>();

	static {
		try {
			EVENTS_MAP.put("HEARTBEAT", FSHeartBeatEvent.class
					.getDeclaredConstructor(String.class, Map.class));
			EVENTS_MAP.put("DTMF", FSDtmfEvent.class.getDeclaredConstructor(
					String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_HANGUP_COMPLETE",
					FSChannelHangupEvent.class.getDeclaredConstructor(
							String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_DATA", FSChannelDataEvent.class
					.getDeclaredConstructor(String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_ANSWER", FSChannelAnsweredEvent.class
					.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_EXECUTE", FSChannelExecuteEvent.class
					.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_EXECUTE_COMPLETE",
					FSChannelExecuteCompleteEvent.class.getConstructor(
							String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_DESTROY", FSChannelDestroyEvent.class
					.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_CREATE", FSChannelCreateEvent.class
					.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_OUTGOING", FSChannelOutgoingEvent.class
					.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_ORIGINATE", FSChannelOriginateEvent.class
					.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_BRIDGE", FSChannelBridgeEvent.class.getConstructor(String.class, Map.class));
			EVENTS_MAP.put("CHANNEL_STATE", FSChannelStateEvent.class.getConstructor(String.class, Map.class));

		} catch (Exception e) {
			log.fatal(e.getMessage());
		}
	}

	static public FSEvent buildEvent(String switchId, String eventStr) {
		Map<String, String> map = new HashMap<String, String>();

		String[] items = eventStr.split("\n");
		for (int i = 0; i < items.length; i++) {
			String[] values = items[i].split(":");
			if (values.length == 2) {
				map.put(values[0].trim(), values[1].trim());
			}
		}
		// log.debug("Got packet {"+ eventStr+"}");
		String eventName = map.get("Event-Name");
		if (eventName != null) {
			if (eventName.equals("CUSTOM")) {
				String eventSubclass = CodecsUtils.urlDecode(map.get("Event-Subclass"));
				if (eventSubclass != null){
					if (eventSubclass.equals("sofia::register")){
						return new FSRegisterEvent (switchId, SigProtocol.SIP, map, true);
					}
					else if (eventSubclass.equals("sofia::unregister")){
						return new FSRegisterEvent(switchId, SigProtocol.SIP, map, false);
					}
					else if (eventSubclass.equalsIgnoreCase("conference::maintenance")){
						String action = map.get("Action");
						if (action.equals("add-member")){
							return new FSConferenceJoinedEvent(switchId, map);
						}						
						else
						if (action.equals("del-member")){
							return new FSConferenceRemovedEvent(switchId, map);
						}
						else
						if (action.equals("start-talking") || action.equals("stop-talking")){
							return new FSConferenceTalkingEvent(switchId, map);
						}
						else
						if (action.equals("mute-member") || action.equals("unmute-member")){
							return new FSConferenceMuteEvent(switchId, map);
						}
					}
					else if (eventSubclass.equals("fifo::info")){
						return new FSQueueEvent (switchId, map);
					}
				}

			} else {
				Constructor ctor = EVENTS_MAP.get(eventName);
				if (ctor == null) {
					return null;
				}

				if (ctor != null) {
					try {
						return (FSEvent) ctor.newInstance(switchId, map);
					} catch (Exception ex) {
						log.fatal(ex.toString(), ex);
					}
				}
			}
		} else {
			String contentType = map.get("Content-Type");
			if (contentType != null) {
				if (contentType.equalsIgnoreCase("auth/request")) {
					return new FSAuthRequestEvent(switchId, map);
				} else if (contentType.equalsIgnoreCase("command/reply")) {
					return new FSCommandReplyEvent(switchId, map);
				} else if (contentType
						.equalsIgnoreCase("text/disconnect-notice")) {
					return new FSSessionDisconnectEvent(switchId, map);
				}
			}
		}
		return null;
	}

	protected Map<String, String> values = new HashMap<String, String>();

	public enum EventType {
		AuthRequest, CommandReply, HeartBeat, ChannelExecute, ChannelExecuteComplete, ChannelData, SessionDisconnect, ChannelDestroy, DTMF, ChannelHangup, ChannelBridge, ChannelAnswered, ChannelCreate, ChannelOutgoing, ChannelState, ChannelOriginate,
		FsRegister, ConferenceJoined, ConferenceRemoved, Queue, ConferenceTalking, ConferenceMute
	}

	EventType eventType;
	String switchId;

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	public FSEvent(String switchId, Map values) {
		this.values = values;
		this.switchId = switchId;
	}

	public String getSwitchId() {
		return this.switchId;
	}

	public String getValue(String key) {
		return values.get(key);
	}

	public String getValue(String key, String defaultValue) {
		if (!values.containsKey(key)) {
			return defaultValue;
		}
		return values.get(key);
	}

	public String toString() {
		return this.getClass().getSimpleName() + ":" + eventType;
	}
}
