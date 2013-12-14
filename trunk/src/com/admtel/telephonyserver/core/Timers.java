package com.admtel.telephonyserver.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.interfaces.TimerNotifiable;

public class Timers extends Thread{
	
	static Logger log = Logger.getLogger(Timers.class);
	
	private static class SingletonHolder{
		private final static Timers instance = new Timers();
	}
	
	public class Timer{
		long duration;
		boolean oneShot;
		TimerNotifiable notifiable;
		long startTime;
		Object data;
		boolean remove;
		public Timer(TimerNotifiable notifiable, long duration, boolean oneShot, Object data) {
			super();
			this.duration = duration;
			this.oneShot = oneShot;
			this.notifiable = notifiable;
			this.startTime = System.currentTimeMillis();
			this.data = data;
			remove = false;
		}
		public long getDuration() {
			return duration;
		}
		public void setDuration(long duration) {
			this.startTime = System.currentTimeMillis();
			this.duration = duration;
		}
		public boolean isOneShot() {
			return oneShot;
		}		
		public TimerNotifiable getNotifiable() {
			return notifiable;
		}
		public boolean update(){
			
			if (this.remove) return false;
			
			if  ((startTime+duration)<=System.currentTimeMillis()){
				if (oneShot){
					this.remove = true;
				}
				else{
					startTime = System.currentTimeMillis();
				}
				return true;
			}
			return false;
		}
		
		
	}
	
	List<Timer> listeners = new ArrayList<Timer>();
	boolean running = true;
	static public long PRECISION = 100; // 100 ms
	
	private Timers(){
		start();
	}
	public static Timers getInstance(){
		return SingletonHolder.instance;
	}
	synchronized public Timer startTimer(TimerNotifiable notifiable, long duration, boolean oneShot, Object data){
		log.trace("Timer " + data + ", for " + notifiable+", added");
		if (duration == 0){
			return null;
		}
		Timer timer= new Timer(notifiable, duration, oneShot, data);
		listeners.add(timer);
		return timer;
	}
	
	public void stopTimer(Timer timer)
	{		
		if (timer != null){
			timer.remove = true;
			log.trace("Timer " + timer + ", for " + timer.getNotifiable()+", removed");
		}
	}
	@Override
	public void run() {
		while (running){
			Iterator<Timer> it = listeners.iterator();
			while (it.hasNext()){
				final Timer timer = it.next();
				if (timer.update()){ //timer expired, fire the event
					AdmThreadExecutor.getInstance().execute(new Runnable(){

						@Override
						public void run() {
							if (timer.getNotifiable().onTimer(timer.data)){
								timer.remove = true;
							}							
						}
						
					});
				}
				if (timer.remove){
					it.remove();
				}
			}
			
			try {
				Thread.sleep(PRECISION); // Substract the time spent doing stuff
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
