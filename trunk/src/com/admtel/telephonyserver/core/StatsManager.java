package com.admtel.telephonyserver.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.events.ChannelEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.utils.LimitedQueue;
import com.google.common.collect.EvictingQueue;

public class StatsManager implements EventListener, TimerNotifiable {

	private static final int UPDATE_INTERVAL = 20; // in ms
	private static final int MAX_CPS_RESULTS = 3;

	EvictingQueue<Double> cps = EvictingQueue.create(MAX_CPS_RESULTS);
	long currentCalls = 0;

	Timer cpsCalculatorTimer;

	private StatsManager() {
		EventsManager.getInstance().addEventListener("STATS_MANAGER", this);
		cpsCalculatorTimer = Timers.getInstance().startTimer(this, UPDATE_INTERVAL * 1000L , false, null);
	}

	private static class SingletonHolder {
		private final static StatsManager instance = new StatsManager();
	}

	public static StatsManager getInstance() {
		return SingletonHolder.instance;
	}

	synchronized private void addChannel(Channel channel) {
		if (channel.getCallOrigin() == CallOrigin.Inbound) {
				currentCalls ++;
		}
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()) {
		//case Alerting:
		case Offered:
			ChannelEvent ce = (ChannelEvent) event;
			addChannel(ce.getChannel());

			break;
		}

		return false;
	}

	@Override
	public boolean onTimer(Object data) {
		// Must be the cps calculator timer

		Double cps = (double)currentCalls / UPDATE_INTERVAL; 
		this.cps.add(cps);
		currentCalls = 0;
		return false;
	}

	public List<Double> getCPS() {
		List<Double> result = new ArrayList<Double>();
		Iterator<Double> it = cps.iterator();
		int counter = 0;
		while (it.hasNext()){
			result.add(it.next());
			counter++;
		}
		for (int i=counter;i<MAX_CPS_RESULTS;i++){
			result.add(i, 0.0);
		}
		return result;
	}

}
