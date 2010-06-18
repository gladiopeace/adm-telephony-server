package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.utils.CodecsUtils;

public class FSDtmfEvent extends FSChannelEvent {

	public FSDtmfEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.DTMF;
	}

	public String getDtmf() {
		String str = values.get("DTMF-Digit");
		if (str == null) return "";
		if (str.startsWith("%")){
			str = CodecsUtils.urlDecode(str);
		}
		return str;
	}

	public long getDuration() {
		String durationStr = values.get("2240");
		if (durationStr != null){
			try{
				return Integer.valueOf(durationStr);
			}
			catch (Exception e){
				
			}
		}
		return 0;
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	
}
