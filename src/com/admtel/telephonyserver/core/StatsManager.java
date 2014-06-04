package com.admtel.telephonyserver.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.events.ChannelEvent;

import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.utils.LimitedQueue;

public class StatsManager implements EventListener, TimerNotifiable {

	private static final long UPDATE_INTERVAL = 60000L; // one minute
	private static final int MAX_CPS_RESULTS = 3;

	LimitedQueue<Date> channelsEventTime = new LimitedQueue(2000);
	LimitedQueue<Double> cps = new LimitedQueue(MAX_CPS_RESULTS);

	Timer cpsCalculatorTimer;

	private StatsManager() {
		EventsManager.getInstance().addEventListener("STATS_MANAGER", this);
		cpsCalculatorTimer = Timers.getInstance().startTimer(this, 60000 /*
																		 * 1
																		 * minute
																		 */, false, null);
	}

	private static class SingletonHolder {
		private final static StatsManager instance = new StatsManager();
	}

	public static StatsManager getInstance() {
		return SingletonHolder.instance;
	}

	private void addChannel(Channel channel) {
		if (channel.getCallOrigin() == CallOrigin.Inbound) {
			synchronized (channelsEventTime) {
				this.channelsEventTime.add(new Date());
			}
		}
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()) {
		case Alerting:
		case Offered:
			ChannelEvent ce = (ChannelEvent) event;
			addChannel(ce.getChannel());

			break;
		}

		return false;
	}

	private double calculateCPS() {
		synchronized (channelsEventTime) {
			BigDecimal result = BigDecimal.ZERO;
			if (channelsEventTime.size() > 0) {
				Date firstEvent = this.channelsEventTime.get(0);
				Date now = new Date();
				if (firstEvent != null) {
					long diff = (now.getTime() - firstEvent.getTime()) / 1000;

					result = new BigDecimal(channelsEventTime.size() / (double) diff);
				}
			}
			return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
	}

	@Override
	public boolean onTimer(Object data) {
		// Must be the cps calculator timer

		// Remove old calls
		synchronized (channelsEventTime) {
			Long now = new Date().getTime();
			boolean remove = false;
			do {
				if (channelsEventTime.size() > 0) {
					Date d = channelsEventTime.getFirst();
					remove = (d != null && (now - d.getTime()) > UPDATE_INTERVAL);
					if (remove) {
						channelsEventTime.removeFirst();
					}
				}
				else {
					remove = false;
				}
			} while (remove);
		}
		
		double cps = calculateCPS();
		this.cps.add(cps);
		return false;
	}

	public List<Double> getCPS() {
		List<Double> result = new ArrayList<Double>();
		for (int i = MAX_CPS_RESULTS - 1; i >= 0; i--) {
			if (cps.size() < i + 1) {
				result.add(0.0);
			} else {
				result.add(cps.get(i));
			}
		}
		return result;
	}

}
