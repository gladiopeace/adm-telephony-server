package com.admtel.telephonyserver.acd;

import java.util.Comparator;
import java.util.Date;
import java.util.Random;


public class AcdAgent {
	public enum Status{Ready, Busy};
	String name;
	String address;
	Status status = Status.Ready;
	Date lastUsedDate = new Date();
	Integer useCounter = 0;
	private AcdChannel channel;
	
	static public DateComparator dateComparator = new DateComparator();
	static public UseComparator useComparator = new UseComparator();
	static public RandomComparator randomComparator = new RandomComparator();
	
	static class DateComparator implements Comparator<AcdAgent>{

		@Override
		public int compare(AcdAgent arg0, AcdAgent arg1) {
			return arg0.lastUsedDate.compareTo(arg1.lastUsedDate);
		}
		
	}
	
	static class UseComparator implements Comparator<AcdAgent>{

		@Override
		public int compare(AcdAgent o1, AcdAgent o2) {
			return o1.useCounter.compareTo(o2.useCounter);
		}
	
	}
	
	static class RandomComparator implements Comparator<AcdAgent>{

		static Random rand = new Random(System.currentTimeMillis());
		@Override
		public int compare(AcdAgent o1, AcdAgent o2) {
			return rand.nextInt(3) - rand.nextInt(3);
		}
		
	}
	
	public AcdAgent(String name, String address) {
		this.name = name;
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		if (status != this.status && status == Status.Busy){
			lastUsedDate = new Date();
			useCounter ++;
		}
		this.status = status;
	}
	public void setChannel(AcdChannel channel) {
		this.channel = channel;
	}
	public AcdChannel getChannel() {
		return channel;
	}
}