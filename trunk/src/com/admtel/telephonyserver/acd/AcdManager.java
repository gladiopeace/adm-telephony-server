package com.admtel.telephonyserver.acd;

import java.util.Date;
import java.util.List;

import com.admtel.telephonyserver.core.*;
import org.apache.log4j.Logger;
import com.admtel.telephonyserver.events.ConnectedEvent;
import com.admtel.telephonyserver.events.DialFailedEvent;
import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.DisconnectedEvent;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;

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
			if (he.getChannel().getUserData("queue") != null) {
				if (he.getChannel().getUserData("agent") == null) { // caller
																	// disconnected
					acdService
							.callerDisconnected(he.getChannel().getUniqueId());
				} else { // agent disconnected
					acdService.agentDisconnected(he.getChannel().getUserData(
							"agent"));
				}
			}
		}
			break;
		case DialStarted: {

			DialStartedEvent dse = (DialStartedEvent) event;
			String dialedAgent = dse.getChannel().getUserData("dialed_agent");
			String queue = dse.getChannel().getUserData("queue");
			log.trace(String.format(
					"Channel (%s) dial started to agent (%s) for queue (%s)",
					dse.getChannel().getUniqueId(), dialedAgent, queue));
			if (dialedAgent != null) {
				dse.getDialedChannel().setUserData("queue", queue);
				dse.getDialedChannel().setUserData("agent", dialedAgent);
				acdService.agentDialStarted(dialedAgent, dse.getDialedChannel()
						.getUniqueId());
			}
		}
			break;
		case Connected: {
			ConnectedEvent ce = (ConnectedEvent) event;

			String agentId = ce.getChannel().getUserData("agent");
			String queueId = ce.getChannel().getUserData("queue");
			log.trace(String.format(
					"Channel (%s) connected queue = %s, agent = %s", ce
							.getChannel().getUniqueId(), queueId, agentId));
			if (ce.getChannel().getUserData("queue") != null
					&& ce.getChannel().getUserData("agent") != null) {
				acdService.agentConnected(agentId);
			}
		}
			break;
		case DialFailed: {
			DialFailedEvent dee = (DialFailedEvent) event;
			String dialedAgent = dee.getChannel().getUserData("dialed_agent");
			if (dialedAgent != null) {
				acdService.agentDialFailed(dialedAgent);
			}
		}
			break;
		}
		return false;
	}

	@Override
	public boolean onTimer(Object data) {
        //TODO Dial request
//		List<DialRequest> dialRequests = acdService.getNextDial();
//		for (DialRequest dialRequest : dialRequests) {
//            RequestManager.getInstance().put(dialRequest);
//		}
		return false;
	}

	public AcdQueue[] getQueues() {
		return acdService.getQueues();
	}

	public AcdAgent[] getAgents() {
		return acdService.getAgents();
	}

	public AcdCall[] getQueueCalls(String queueId) {
		return acdService.getQueueCalls(queueId);
	}
	public AcdAgent getAgentById(String agentId){
		return acdService.getAgentById(agentId);
	}
	public AcdAgent getAgentByName(String agentName){
		return acdService.getAgentByName(agentName);
	}
}
