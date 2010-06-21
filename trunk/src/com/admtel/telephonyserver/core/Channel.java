package com.admtel.telephonyserver.core;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.DtmfEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.radius.RadiusServers;
import com.admtel.telephonyserver.registrar.UserLocation;
import com.admtel.telephonyserver.utils.AdmUtils;

public abstract class Channel {

	static Logger log = Logger.getLogger(Channel.class);

	public enum State {
		Null, InboundAlerting, Idle, Clearing, Answering, OutboundAlerting, MediaBusy, Busy, Conferenced,
	}

	public enum CallOrigin {
		Inbound, Outbound
	};

	private List<EventListener> listeners = new CopyOnWriteArrayList<EventListener>();

	protected State state = State.Idle;
	protected String id;
	protected String dtmfBuffer = "";
	protected Switch _switch;

	protected String uniqueId;

	private ChannelData channelData = new ChannelData();
	protected Participant conferenceParticipant; // information about the
													// channel when joined in a
													// conference bridge

	protected DateTime createdTime = new DateTime();
	protected CallOrigin callOrigin = CallOrigin.Inbound;
	// Radius needed attributes	
	protected DateTime setupTime;
	protected DateTime hangupTime;
	protected DateTime answerTime;

	protected String h323CallOrigin;

	protected String acctUniqueSessionId = UUID.randomUUID().toString();
	protected String acctSessionId;
	protected String serviceType = "Login-User";
	protected Integer h323DisconnectCause=16;//normal call clearing

	public long getSessionTime() {
		if (answerTime == null)
			return 0;

		if (hangupTime != null) {
			return new Duration(answerTime, hangupTime).getStandardSeconds();
		}
		return new Duration(answerTime, new DateTime()).getStandardSeconds();
	}

	public String getServiceNumber(){
		return getChannelData().getServiceNumber();
	}
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public CallOrigin getCallOrigin() {
		return callOrigin;
	}

	public void setCallOrigin(CallOrigin callOrigin) {
		this.callOrigin = callOrigin;
	}

	public DateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(DateTime createdTime) {
		this.createdTime = createdTime;
	}

	public String getUserName() {
		return getChannelData().getUserName();
	}

	public void setUserName(String userName) {
		getChannelData().setUserName(userName);
	}

	public DateTime getSetupTime() {
		return setupTime;
	}

	public void setSetupTime(DateTime setupTime) {
		this.setupTime = setupTime;
	}

	public DateTime getHangupTime() {
		return hangupTime;
	}

	public void setHangupTime(DateTime hangupTime) {
		this.hangupTime = hangupTime;
	}

	public String getH323CallOrigin() {
		return h323CallOrigin;
	}

	public void setH323CallOrigin(String h323CallOrigin) {
		this.h323CallOrigin = h323CallOrigin;
	}

	public String getH323RemoteAddress() {
		return getChannelData().getRemoteIP();
	}
	public String getAcctUniqueSessionId() {
		return acctUniqueSessionId;
	}

	public void setAcctUniqueSessionId(String acctUniqueSessionId) {
		this.acctUniqueSessionId = acctUniqueSessionId;
	}

	public String getAcctSessionId() {
		return acctSessionId;
	}

	public void setAcctSessionId(String acctSessionId) {
		this.acctSessionId = acctSessionId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getCalledStationId() {
		return getChannelData().getCalledNumber();
	}

	public String getCallingStationId() {
		return getChannelData().getCallerIdNumber();
	}

	public void addEventListener(EventListener listener) {
		getListeners().add(listener);
	}

	public void removeEventListener(EventListener listener) {
		getListeners().remove(listener);
	}

	public void removeAllEventListeners() {
		getListeners().clear();
	}

	public Channel(Switch _switch, String id) {
		this._switch = _switch;
		this.id = id;
		this.uniqueId = UUID.randomUUID().toString();
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public Switch getSwitch() {
		return this._switch;
	}

	public void setDtmfBuffer(String dtmfBuffer) {
		this.dtmfBuffer = dtmfBuffer;
	}

	public String getId() {
		return id;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return this.state;
	}

	public String getDtmfBuffer() {
		return dtmfBuffer;
	}

	public abstract Result internalPlayAndGetDigits(int max, String prompt,
			long timeout, String terminators, boolean interruptPlay);

	public abstract Result internalHangup(Integer integer);

	public abstract Result internalPlayback(String[] prompt, String terminators);

	public abstract Result internalPlayback(String prompt, String terminators);

	public abstract Result internalAnswer();

	public abstract Result internalPlayAndGetDigits(int max, String[] prompt,
			long timeout, String terminators, boolean interruptPlay);

	public abstract Result internalDial(String address, long timeout);

	final public Result playAndGetDigits(int max, String prompt, long timeout,
			String terminators) {
		if (state != State.Idle) {
			log.warn(String.format(
					"Channel (%s), playAndGetDigits, invalid state(%s)", this,
					state));
			return Result.ChannelInvalidState;
		}
		Result result = internalPlayAndGetDigits(max, prompt, timeout,
				terminators, true);
		if (result == Result.Ok) {
			state = State.MediaBusy;
		}
		return result;
	}

	final public Result hangup(DisconnectCode disconnectCode) {
		Result result = internalHangup(disconnectCode.toInteger());
		if (result == Result.Ok) {
			state = State.Clearing;
		}
		return result;
	}

	final public Result playback(String[] prompt, String terminators) {
		if (state != State.Idle) {
			log.warn(String.format("Channel (%s), playback, invalid state(%s)",
					this, state));
			return Result.ChannelInvalidState;
		}
		Result result = internalPlayback(prompt, terminators);
		if (result == Result.Ok) {
			state = State.MediaBusy;
		}
		return result;
	}

	final public Result playback(String prompt, String terminators) {
		if (state != State.Idle) {
			log.warn(String.format("Channel (%s), playback, invalid state(%s)",
					this, state));
			return Result.ChannelInvalidState;
		}
		Result result = internalPlayback(prompt, terminators);
		if (result == Result.Ok) {
			state = State.MediaBusy;
		}
		return result;

	}

	final public Result answer() {
		if (state != State.InboundAlerting) {
			log.warn(String.format("Channel (%s), answer, invalid state(%s)",
					this, state));
			return Result.ChannelInvalidState;
		}
		Result result = internalAnswer();
		if (result == Result.Ok) {
			state = State.Answering;
		}
		return result;

	}

	final public Result joinConference(String conferenceId, boolean moderator,
			boolean startMuted, boolean startDeaf) { // TODO add more parameters
		if (state != State.Idle) {
			log.warn(String.format(
					"Channel(%s), joinConference invalid state(%s)", this,
					state));
			return Result.ChannelInvalidState;
		}
		state = State.Conferenced;
		Result result = internalJoinConference(conferenceId, moderator,
				startMuted, startDeaf);
		if (result != Result.Ok) {
			state = State.Idle;
		}
		return result;
	}

	public abstract Result internalJoinConference(String conferenceId,
			boolean moderator, boolean startMuted, boolean startDeaf);

	final public Result playAndGetDigits(int max, String[] prompt,
			long timeout, String terminators) {
		if (state != State.Idle) {
			log.warn(String.format(
					"Channel (%s), playAndGetDigits, invalid state(%s)", this,
					state));
			return Result.ChannelInvalidState;
		}
		state = State.MediaBusy;
		Result result = internalPlayAndGetDigits(max, prompt, timeout,
				terminators, true);
		if (result != Result.Ok) {
			state = State.Idle;
		}
		return result;

	}

	final public Result dial(UserLocation userLocation, long timeout) {
		Result result = Result.Ok;
		if (userLocation.getSwitchId().equals(
				this.getSwitch().getDefinition().getId())) {

			// TODO have more dynamic protocols to dial out

			result = internalDial("sip:" + userLocation.getUser(), timeout);
		} else {
			result = internalDial(String.format("sip:%s@%s", userLocation
					.getUser(), getSwitch().getDefinition().getAddress()),
					timeout);
		}

		return result;
	}

	final public Result dial(String address, long timeout) {
		/*
		 * if (state != State.Idle){
		 * log.warn(String.format("Channel (%s), originate, invalid state(%s)",
		 * this, state)); return Result.ChannelInvalidState; }
		 */
		Result result = internalDial(address, timeout);
		return result;
	}

	public boolean onEvent(Event e) {
		if (e == null)
			return true;
		log.trace("Channel " + this + ", got event " + e);
		switch (e.getEventType()) {
		case DTMF: {
			DtmfEvent event = (DtmfEvent) e;
			dtmfBuffer += event.getDigit();
		}
			break;
		case PlaybackEnded:
		case PlayAndGetDigitsEnded:
		case AnswerFailed:
		case PlaybackFailed:
		case PlayAndGetDigitsFailed:
			state = State.Idle;
			break;
		case Answered:
			state = State.Idle;
			answerTime = new DateTime();
			break;
		case InboundAlerting: {
			InboundAlertingEvent ie = (InboundAlertingEvent) e;
			ie.setCalledIdNumber(getChannelData().getCalledNumber());
			ie.setCallerIdNumber(getChannelData().getCallerIdNumber());
			ie.setServiceNumber(getChannelData().getServiceNumber());
			state = State.InboundAlerting;
			callOrigin = CallOrigin.Inbound;
			acctSessionId = UUID.randomUUID().toString();
			setupTime = new DateTime();
			this.addEventListener(EventsManager.getInstance());
		}
			break;
		case Hangup:
		{
			HangupEvent he = (HangupEvent) e;
			hangupTime = new DateTime();
			h323DisconnectCause = he.getHangCause();
		}
			break;
		case OutboundAlerting:
			state = State.OutboundAlerting;
			callOrigin = CallOrigin.Outbound;
			acctSessionId = UUID.randomUUID().toString();
			setupTime = new DateTime();
			this.addEventListener(EventsManager.getInstance());
			break;
			
		}
		
		try {
			Iterator<EventListener> it = getListeners().iterator();
			while (it.hasNext()) {
				it.next().onEvent(e);
			}
		} catch (Exception ex) {
			log.fatal(AdmUtils.getStackTrace(ex));
		}
		if (e.getEventType() == EventType.Hangup) {
			removeAllEventListeners();
		}
		return false;
	}

	public DateTime getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(DateTime answerTime) {
		this.answerTime = answerTime;
	}

	@Override
	public String toString() {
		return "Channel [_switch=" + _switch + ", id=" + id + ", state="
				+ state + "]";
	}

	public Integer getH323DisconnectCause() {
		return this.h323DisconnectCause;
	}

	public void setListeners(List<EventListener> listeners) {
		this.listeners = listeners;
	}

	public List<EventListener> getListeners() {
		return listeners;
	}

	public String getLoginIP() {
		return getChannelData().getLoginIP();
	}

	public void setChannelData(ChannelData channelData) {
		this.channelData = channelData;
	}

	public ChannelData getChannelData() {
		return channelData;
	}
}
