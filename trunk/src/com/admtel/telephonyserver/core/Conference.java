package com.admtel.telephonyserver.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.joda.time.DateTime;

import com.admtel.telephonyserver.events.ConferenceJoinedEvent;
import com.admtel.telephonyserver.events.ConferenceLeftEvent;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;

public class Conference implements TimerNotifiable{
	String id;	
	DateTime createTime;
	
	Map<Channel, Participant> participants = new HashMap<Channel, Participant>();
	Map<Channel, Participant> synchronizedParticipants = Collections.synchronizedMap(participants);
	
	public Conference(String id){
		this.id = id;
		createTime = new DateTime();
		Timers.getInstance().startTimer(this, 10000, true, null);
	}

	@Override
	public boolean onTimer(Object data) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onConferenceJoined(ConferenceJoinedEvent cje) {
		
		Participant p = new Participant(cje.getParticipantId(), cje.isModerator(), cje.isMuted(), cje.isDeaf());
		p.setJoinTime(new DateTime());
		synchronizedParticipants.put(cje.getChannel(), p);
		
	}

	public void onConferenceLeft(ConferenceLeftEvent cle) {
		synchronizedParticipants.remove(cle.getChannel());				
	}
	public long getParcitipantsCount(){
		return participants.size();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(DateTime createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		final int maxLen = 8;
		return (createTime != null ? "createTime=" + createTime + " \\n, " : "")
				+ (id != null ? "id=" + id + " \\n, " : "")
				+ (participants != null ? "participants="
						+ toString(participants.entrySet(), maxLen) : "");
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	public Participant getParticipant(Channel channel) {
		return participants.get(channel);
	}
	
}
