package com.admtel.telephonyserver.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.ConferenceJoinedEvent;
import com.admtel.telephonyserver.events.ConferenceLeftEvent;
import com.admtel.telephonyserver.events.ConferenceMutedEvent;
import com.admtel.telephonyserver.events.ConferenceTalkEvent;
import com.admtel.telephonyserver.events.DisconnectCode;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.radius.RadiusServers;

public class ConferenceManager implements TimerNotifiable, EventListener{
	
	static Logger log = Logger.getLogger(ConferenceManager.class);
	
	Map<String, Conference> conferences = new HashMap<String, Conference>();
	Map<String, Conference> synchronizedConferences = Collections.synchronizedMap(conferences);
	
	private ConferenceManager(){
		Timers.getInstance().startTimer(this, 5000, true, null);
	}
		
	private static class SingletonHolder {
		private final static ConferenceManager instance = new ConferenceManager();
	}
	
	public static ConferenceManager getInstance(){
		return SingletonHolder.instance;
	}
	public Conference getConferenceById(String id){
		return conferences.get(id);
	}

	@Override
	public boolean onTimer(Object data) {
		return true;
	}
	
	public Collection<Conference> getAll(){
		return conferences.values();
	}

	public String getSwitchId() {
		return null;
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()){
		case ConferenceJoined:{
			
			log.trace(event);
			
			ConferenceJoinedEvent cje = (ConferenceJoinedEvent) event;
			Conference c = conferences.get(cje.getConferenceId());
						
			if (c == null){
				c = new Conference (cje.getConferenceId());
				synchronizedConferences.put(cje.getConferenceId(), c);
			}
			c.onConferenceJoined (cje);

			RadiusServers.getInstance().accountingStart(cje.getChannel(), c, c.getParticipant(cje.getParticipantId()));

		}
			break;
		case ConferenceLeft:{
			
			log.trace(event);
			
			ConferenceLeftEvent cle = (ConferenceLeftEvent) event;
			Conference c = conferences.get(cle.getConferenceId());
			
			
			if (c != null){
				RadiusServers.getInstance().accountingStop(cle.getChannel(), c, c.getParticipant(cle.getParticipantId()));
				c.onConferenceLeft(cle);
				if (c.getParcitipantsCount() == 0){
					synchronizedConferences.remove(c.getId());
				}
			}
		}
			break;
			
		case ConferenceTalk:
		{
			ConferenceTalkEvent cte = (ConferenceTalkEvent)event;
			Conference c = conferences.get(cte.getConferenceId());
			if (c != null){
				c.onConferenceTalk(cte);
			}
		}
			break;
		case ConferenceMuted:
		{
			ConferenceMutedEvent cme = (ConferenceMutedEvent) event;
			Conference c = conferences.get(cme.getConferenceId());
			if (c != null){
				c.onConferenceMuted(cme);
			}
		}
			break;
		}
		return false;
	}
	public Result disconnectParticipant(String conferenceId, String participantId){
		Conference conference = conferences.get(conferenceId);
		if (conference == null){
			log.warn(String.format("Conferece not found %s", conferenceId));
			return Result.InvalidConference;
		}
		Participant participant = conference.getParticipant(participantId);
		if (participant == null){
			log.warn(String.format("Participant %s , not found", participantId));
			return Result.InvalidParticipant;
		}
		return participant.getChannel().hangup(DisconnectCode.Normal);
	}
	public Result muteParticipant(String conferenceId, String participantId){
	
		return Result.Ok;
	}
}
