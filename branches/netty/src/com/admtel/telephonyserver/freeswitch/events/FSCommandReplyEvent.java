package com.admtel.telephonyserver.freeswitch.events;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FSCommandReplyEvent extends FSEvent{
	
	static Pattern PATTERN = Pattern.compile("(\\+OK|-ERR)(.*)");
	
	
	boolean success = false;
	String resultDescription ="";
	public FSCommandReplyEvent(String switchId, Map values) {
		super(switchId, values);
		eventType = EventType.CommandReply;
		String replyText = (String) values.get("Reply-Text");
		if (replyText != null){
			
			Matcher matcher = PATTERN.matcher(replyText);
			
			if (matcher.matches()){
				if (matcher.group(1).trim().equalsIgnoreCase("+OK")){
					success = true;
				}
				resultDescription = matcher.group(2).trim();
			}			
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public String getResultDescription() {
		return resultDescription;
	}
	public String toString(){
		return super.toString() +":"+success+":"+resultDescription;
	}
}
