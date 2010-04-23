package com.admtel.telephonyserver.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.admtel.telephonyserver.events.ConferenceLeftEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;

public class Conference implements EventListener, TimerNotifiable{
	String id;	
	DateTime createTime;
	
	Map<String, Channel> channels = new HashMap<String, Channel>();
	Map<String, Channel> synchronizedChannels = Collections.synchronizedMap(channels);
	
	public Conference(String id){
		this.id = id;
		createTime = new DateTime();
		Timers.getInstance().startTimer(this, 10000, true, null);
	}
	
	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()){
		case ConferenceJoined:
			break;
		case ConferenceLeft:{
			ConferenceLeftEvent cle = (ConferenceLeftEvent) event;
			removeChannel(cle.getChannel());
		}
			break;
		}
		return false;
	}
	public String getId() {
		return id;
	}

	public void removeChannel(Channel channel){
		if (channel != null && channels.containsKey(channel.getUniqueId())){
			channel.removeEventListener(this);
			synchronizedChannels.remove(channel.getUniqueId());
		}
	}
	public void addChannel (Channel channel){
		if (channel != null){
			if (!channels.containsKey(channel.getUniqueId())){
				synchronizedChannels.put(channel.getUniqueId(), channel);
				channel.addEventListener(this);
			}
			
		}
		return;
	}
	@Override
	public boolean onTimer(Object data) {
		return true;
	}

	public String dump() {
		String result="";
		Iterator<Channel> it = channels.values().iterator();
		while (it.hasNext()){
			Channel c = it.next();
			result+=String.format("\t%s\t%s\t%s\tTalking(%s)\n", c.getSwitch().definition.getId(), c.getId(), c.conferenceParticipant.memberId, c.conferenceParticipant.talking);
		}
		return result;
	}

	public String getSwitchId() {
		return null;
	}	
}
