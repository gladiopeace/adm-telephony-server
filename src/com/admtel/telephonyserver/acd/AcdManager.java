package com.admtel.telephonyserver.acd;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.acd.impl.AcdAgent;
import com.admtel.telephonyserver.acd.impl.AcdChannel;
import com.admtel.telephonyserver.acd.impl.AcdDataProviderImpl;
import com.admtel.telephonyserver.acd.impl.AcdQueue;
import com.admtel.telephonyserver.acd.impl.AcdServiceImpl;
import com.admtel.telephonyserver.core.BeansManager;
import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.EventsManager;
import com.admtel.telephonyserver.core.Result;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.core.Switches;
import com.admtel.telephonyserver.core.Timers;
import com.admtel.telephonyserver.events.AcdQueueFailedEvent;
import com.admtel.telephonyserver.events.DialFailedEvent;
import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.DialStatus;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.DisconnectedEvent;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.requests.DialRequest;

public class AcdManager implements EventListener, TimerNotifiable {

	static Logger log = Logger.getLogger(AcdManager.class);
	public AcdService acdService;
	public int timeout = 1000;

	public AcdManager() {

	}

	static public AcdManager getInstance() {
		return (AcdManager) BeansManager.getInstance().getBean("AcdManager");
	}

	public Result queueChannel(String queueName, String channelId,
			Date setupDate, int priority) {
		Result result = Result.RequestError;
		if (acdService.queueChannel(queueName, channelId, setupDate, priority)) {
			return Result.Ok;
		}
		return result;
	}

	public void init() {
		EventsManager.getInstance().addEventListener("ACD_Manager", this);
		log.trace("Initializing ACD Manager");
		Timers.getInstance().startTimer(this, timeout, false, null);
	}

	@Override
	public boolean onEvent(Event event) {
		switch (event.getEventType()) {
		case Disconnected: {
			DisconnectedEvent he = (DisconnectedEvent) event;
			// Caller disconnected
			if (acdService.containsChannel(he.getChannel().getUniqueId())) {
				acdService.unqueueChannel(he.getChannel().getUniqueId());
			}

			else if (he.getChannel().getOtherChannel() != null) {
				// Agent disconnected
				acdService.requeueChannel(he.getChannel().getOtherChannel()
						.getUniqueId());

			}
		}
			break;
		case DialFailed: {
			DialFailedEvent dee = (DialFailedEvent) event;
			if (dee.getChannel().getCallState() == Channel.CallState.AcdQueued) {
				acdService.requeueChannel(dee.getChannel().getUniqueId());
			}
		}
			break;
		}
		return false;
	}

	public Map<String, AcdQueue> getQueues() {
		return acdService.getQueues();
	}

	public AcdAgent getAgent(String agentId){
		return acdService.getAgent(agentId);
	}
	public Queue<AcdChannel> getQueuedChannels(String queueId) {
		return acdService.getQueuedChannels(queueId);
	}
	public AcdChannel getChannelForAgent(String agentId){
		return acdService.getChannelForAgent(agentId);
	}

	@Override
	public boolean onTimer(Object data) {
		List<DialRequest> dialRequests = acdService.getNextDial();
		for (DialRequest dialRequest : dialRequests) {
			Switches.getInstance().processRequest(dialRequest);
		}
		return false;
	}

}
