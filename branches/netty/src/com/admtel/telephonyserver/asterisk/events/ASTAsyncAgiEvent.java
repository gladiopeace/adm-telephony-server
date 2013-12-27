package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

import com.admtel.telephonyserver.misc.VariableMap;
import com.admtel.telephonyserver.utils.CodecsUtils;

public class ASTAsyncAgiEvent extends ASTChannelEvent {

	public ASTAsyncAgiEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.AsyncAgi;
		String env = CodecsUtils.urlDecode(values.get("Env"));
		VariableMap vars = new VariableMap();
		vars.addDelimitedVars(env, ": ","\n");
		this.values.putAll(vars);
	}

	@Override
	public String getChannelId() {
		return values.get("Channel");
	}
	public String getSubEvent(){
		return values.get("SubEvent");
	}
	public boolean isStartAgi(){
		String subEvent = getSubEvent();
		return (subEvent != null && subEvent.equals("Start"));
	}
}
