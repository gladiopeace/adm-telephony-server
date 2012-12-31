package com.admtel.telephonyserver.asterisk.events;

import java.util.Map;

import com.admtel.telephonyserver.events.DialStatus;

public class ASTDialEvent extends ASTChannelEvent {

	public ASTDialEvent(String switchId, Map<String, String> values) {
		super(switchId, values);
		eventType = EventType.Dial;
	}
	public String getDestinationChannel(){
		if (values.containsKey("Destination")){
			return values.get("Destination");
		}
		return "";
	}
	public boolean isBegin(){
		String subEvent = values.get("SubEvent");
		return subEvent != null && subEvent.equals("Begin");
	}
	public DialStatus getDialStatus(){
		DialStatus result = DialStatus.Unknown;
		String dialStatus = values.get("DialStatus");
		if (dialStatus != null){
			if (dialStatus.equals("CONGESTION")){
				result = DialStatus.Congested;
			}
			else
			if (dialStatus.equals("NOANSWER")){
				result = DialStatus.NoAnswer;
			}
			else
			if (dialStatus.equals("BUSY")){
				result = DialStatus.Busy;
			}
			else
			if (dialStatus.equals("CANCEL")){
				result = DialStatus.Cancel;
			}
			else
			if (dialStatus.equals("ANSWER")){
				result = DialStatus.Answer;
			}
			else
			if (dialStatus.equals("CHANUNAVAIL")){
				result = DialStatus.NoCircuit;
			}
		}
		return result;
	}
	@Override
	public String toString() {
		return "ASTDialEvent ["
				+ (getDestinationChannel() != null ? "getDestinationChannel()="
						+ getDestinationChannel() + ", " : "")
				+ "isBegin()="
				+ isBegin()
				+ ", "
				+ (getDialStatus() != null ? "getDialStatus()="
						+ getDialStatus() : "") + "]";
	}
	
}
