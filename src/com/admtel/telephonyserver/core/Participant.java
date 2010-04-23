package com.admtel.telephonyserver.core;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Participant {

	boolean muted;
	boolean moderator;
	boolean deaf;	
	String memberId; //Given to us by the switch
	DateTime joinTime;
	boolean talking;
	
	String uniqueId;
	
	public boolean isMuted() {
		return muted;
	}
	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	public boolean isModerator() {
		return moderator;
	}
	public void setModerator(boolean moderator) {
		this.moderator = moderator;
	}
	public boolean isDeaf() {
		return deaf;
	}
	public void setDeaf(boolean deaf) {
		this.deaf = deaf;
	}
	public void setTalking(boolean talking){
		this.talking = talking;
	}
	public boolean isTalking(){
		return this.talking;
	}
	public Participant(boolean moderator, boolean muted, boolean deaf) {
		super();		
		this.muted = muted;
		this.moderator = moderator;
		this.deaf = deaf;
		this.uniqueId = UUID.randomUUID().toString();
	}
	public void setMemeber(String usernum) {
		this.memberId = usernum;
		
	}
	public void setJoinTime(DateTime dateTime) {
		this.joinTime = dateTime;
		
	}
	public String getUniqueId(){
		return this.uniqueId;
	}
}
