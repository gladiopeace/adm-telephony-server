package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;

import com.admtel.telephonyserver.freeswitch.commands.FSApplication;
import com.admtel.telephonyserver.utils.CodecsUtils;

public class FSChannelExecuteEvent extends FSChannelEvent {

	FSApplication application;
	String applicationData;
	
	public FSChannelExecuteEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.ChannelExecute;
		application = FSApplication.valueOf((String)values.get("Application"));
		applicationData = CodecsUtils.urlDecode((String) values.get("Application-Data"));
	}

	@Override
	public String getChannelId() {
		return values.get("Unique-ID");
	}
	
	@Override
	public String toString() {
		return "FSChannelExecuteEvent ["
				+ (application != null ? "application=" + application + ", "
						: "")
				+ (applicationData != null ? "applicationData="
						+ applicationData + ", " : "")
				+ (super.toString() != null ? "toString()=" + super.toString()
						: "") + "]";
	}

}
