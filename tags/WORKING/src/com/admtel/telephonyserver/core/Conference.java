package com.admtel.telephonyserver.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.admtel.telephonyserver.events.ConferenceDeafenedEvent;
import com.admtel.telephonyserver.events.ConferenceJoinedEvent;
import com.admtel.telephonyserver.events.ConferenceLeftEvent;
import com.admtel.telephonyserver.events.ConferenceMutedEvent;
import com.admtel.telephonyserver.events.ConferenceTalkEvent;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;

public class Conference implements TimerNotifiable {
	
	enum ConferenceState { Enabled, Locked};
	public String id;
	public DateTime createTime;
	public ConferenceState state = ConferenceState.Enabled;

	Map<String, Participant> participants = new HashMap<String, Participant>();
	Map<String, Participant> synchronizedParticipants = Collections
			.synchronizedMap(participants);

	String switchId;
	public Conference(String switchId, String id) {
		this.id = id;
		this.switchId = switchId;
		createTime = new DateTime();
		Timers.getInstance().startTimer(this, 10000, true, null);
	}

	public List<Participant> getParticipants(){
		List<Participant> result = new ArrayList<Participant>();
		result.addAll(synchronizedParticipants.values());
		return result;
	}
	@Override
	public boolean onTimer(Object data) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onConferenceJoined(ConferenceJoinedEvent cje) {

		Participant p = new Participant(cje.getChannel(), this, cje
				.getParticipantId(), cje.isModerator(), cje.isMuted(), cje
				.isDeaf());
		p.setJoinTime(new DateTime());
		synchronizedParticipants.put(cje.getParticipantId(), p);

	}

	public void onConferenceLeft(ConferenceLeftEvent cle) {
		synchronizedParticipants.remove(cle.getParticipantId());
	}

	public long getParcitipantsCount() {
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

	public ConferenceState getState() {
		return state;
	}

	public void setState(ConferenceState state) {
		this.state = state;
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

	public Participant getParticipant(String participantId) {
		return participants.get(participantId);
	}

	public void onConferenceTalk(ConferenceTalkEvent cte) {
		Participant p = participants.get(cte.getParticipantId());
		if (p!=null){
			p.setTalking(cte.isTalking());
		}
		
	}

	public void onConferenceMuted(ConferenceMutedEvent cme) {
		Participant p = participants.get(cme.getParticipantId());
		if (p != null){
			p.setMuted(cme.isMuted());
		}
		
	}
	public void onConferenceDeafened(ConferenceDeafenedEvent cde){
		Participant p = participants.get(cde.getParticipantId());
		if (p != null){
			p.setDeaf(cde.isDeafened());
		}
	}

	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}

	public void lock() {
		this.state = ConferenceState.Locked;
		
	}
	public void unlock(){
		this.state = ConferenceState.Enabled;
	}
	public boolean islocked(){
		return state == ConferenceState.Locked;
	}
}
