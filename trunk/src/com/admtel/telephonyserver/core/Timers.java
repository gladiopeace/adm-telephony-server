package com.admtel.telephonyserver.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.interfaces.TimerNotifiable;

public class Timers extends Thread {

	static Logger log = Logger.getLogger(Timers.class);

	private static class SingletonHolder {
		private final static Timers instance = new Timers();
	}

	public class Timer {
		long duration;

		@Override
		public String toString() {
			return "Timer [duration=" + duration + ", oneShot=" + oneShot + "]";
		}

		boolean oneShot;
		TimerNotifiable notifiable;
		long startTime;
		Object data;
		boolean remove;

		public Timer(TimerNotifiable notifiable, long duration,
				boolean oneShot, Object data) {
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

		public boolean update() {

			if (this.remove)
				return false;

			if ((startTime + duration) <= System.currentTimeMillis()) {
				if (oneShot) {
					this.remove = true;
				} else {
					startTime = System.currentTimeMillis();
				}
				return true;
			}
			return false;
		}

	}

	List<Timer> listeners = new CopyOnWriteArrayList<Timer>();
	boolean running = true;
	static public long PRECISION = 100; // 100 ms

	private Timers() {
		start();
	}

	public static Timers getInstance() {
		return SingletonHolder.instance;
	}

	synchronized public Timer startTimer(TimerNotifiable notifiable,
			long duration, boolean oneShot, Object data) {
		if (duration == 0) {
			return null;
		}
		Timer timer = new Timer(notifiable, duration, oneShot, data);
		log.trace("Added Timer " + timer.toString() + ", for " + notifiable);
		listeners.add(timer);
		return timer;
	}

	public void stopTimer(Timer timer) {
		if (timer != null) {
			timer.remove = true;
			log.trace("Removed Timer " + timer.toString() + ", for "
					+ timer.getNotifiable());
		}
	}

	@Override
	public void run() {
		while (running) {
			try {
				Iterator<Timer> it = listeners.iterator();
				while (it.hasNext()) {
					final Timer timer = it.next();
					if (timer.update()) { // timer expired, fire the event
						AdmThreadExecutor.getInstance().execute(new Runnable() {

							@Override
							public void run() {
								try {
									if (timer.getNotifiable() != null) {
										if (timer.getNotifiable().onTimer(
												timer.data)) {
											timer.remove = true;
										}
									} else {
										timer.remove = true;
									}
								} catch (Exception e) {
									timer.remove = true;
									log.fatal(e.getMessage(), e);
								}
							}

						});
					}
					if (timer.remove) {
						listeners.remove(timer);
					}
				}

				try {
					Thread.sleep(PRECISION);
				} catch (InterruptedException e) {
					log.fatal(e.getMessage(), e);
				}
			} catch (Exception e) {
				log.fatal(e.getMessage(), e);
			}
		}
	}

}
