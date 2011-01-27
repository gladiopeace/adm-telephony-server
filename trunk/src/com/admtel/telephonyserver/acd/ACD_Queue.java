package com.admtel.telephonyserver.acd;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.admtel.telephonyserver.core.Channel;

public class ACD_Queue {
	String name;
	
	Queue<Channel> channels = new ConcurrentLinkedQueue<Channel>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
