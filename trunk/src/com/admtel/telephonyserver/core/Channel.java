package com.admtel.telephonyserver.core;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.admtel.telephonyserver.config.SystemConfig;
import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.events.ConferenceJoinedEvent;
import com.admtel.telephonyserver.events.ConferenceLeftEvent;
import com.admtel.telephonyserver.events.DtmfEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.radius.RadiusServers;
import com.admtel.telephonyserver.registrar.UserLocation;
import com.admtel.telephonyserver.requests.AnswerRequest;
import com.admtel.telephonyserver.requests.DialRequest;
import com.admtel.telephonyserver.requests.HangupRequest;
import com.admtel.telephonyserver.requests.ParticipantMuteRequest;
import com.admtel.telephonyserver.requests.Request;
import com.admtel.telephonyserver.utils.AdmUtils;
import com.admtel.telephonyserver.utils.PromptsUtils;

public abstract class Channel implements TimerNotifiable {

	static Logger log = Logger.getLogger(Channel.class);

	public enum State {
		Null, InboundAlerting, Idle, Clearing, Answering, OutboundAlerting, MediaBusy, Busy, Conferenced, Queued,
	}

	private enum TimersDefs {
		HangupTimer, InterimUpdateTimer
	}

	Timer hangupTimer;
	Timer interimUpdateTimer;

	public enum CallOrigin {
		Inbound, Outbound
	};

	private List<EventListener> listeners = new CopyOnWriteArrayList<EventListener>();

	protected State state = State.Idle;
	protected String id;
	protected String dtmfBuffer = "";
	protected Switch _switch;

	private String uniqueId;

	private ChannelData channelData = new ChannelData();

	protected DateTime createdTime = new DateTime();
	protected CallOrigin callOrigin = CallOrigin.Inbound;
	// Radius needed attributes
	protected DateTime setupTime;
	protected DateTime hangupTime;
	protected DateTime answerTime;

	protected String h323CallOrigin;

	protected String acctUniqueSessionId;
	protected Integer h323DisconnectCause = 16;// normal call clearing

	protected String baseDirectory = SystemConfig.getInstance().serverDefinition
			.getBaseDirectory();

	protected Locale language;

	private String conferenceId;
	private String memberId;
	
	public Script script;

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {		
		if (this.script != null){
			this.script.onStop();
		}
		this.script = script;
	}

	private MessageHandler messageHandler = new QueuedMessageHandler() {

		@Override
		public void onMessage(Object message) {
			if (message instanceof Request) {
				Channel.this.processRequest((Request) message);
			} else
				Channel.this.processNativeEvent(message);

		}

	};

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}

	public void setHangupAfter(long msTimeout) {
		hangupTimer = Timers.getInstance().startTimer(this, msTimeout, true,
				TimersDefs.HangupTimer);
	}

	public long getSessionTime() {
		if (answerTime == null)
			return 0;

		if (hangupTime != null) {
			return new Duration(answerTime, hangupTime).getStandardSeconds();
		}
		return new Duration(answerTime, new DateTime()).getStandardSeconds();
	}

	public String getServiceNumber() {
		return getChannelData().getServiceNumber();
	}

	public void setServiceNumber(String serviceNumber) {
		getChannelData().setServiceNumber(serviceNumber);
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
		return uniqueId;
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
		language = Locale.ENGLISH;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public Switch getSwitch() {
		return this._switch;
	}

	public void setSwitch(Switch _switch) {
		this._switch = _switch;
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

	public abstract Result internalQueue(String queueName, boolean agent);

	final public Result queue(String queueName, boolean agent) { // TODO add
																	// more
																	// parameters
		Result result = internalQueue(queueName, agent);

		return result;
	}

	final public Result playAndGetDigits(int max, String prompt, long timeout,
			String terminators) {
		if (state != State.Idle) {
			log.warn(String.format(
					"Channel (%s), playAndGetDigits, invalid state(%s)", this,
					state));
			return Result.ChannelInvalidState;
		}
		prompt = PromptsUtils.prepend(prompt, baseDirectory, "/sounds/",
				language.toString(), "/");
		Result result = internalPlayAndGetDigits(max, prompt, timeout,
				terminators, true);
		if (result == Result.Ok) {
			state = State.MediaBusy;
		}
		return result;
	}

	final public Result hangup(DisconnectCode disconnectCode) {
		Result result = internalHangup(disconnectCode.ordinal());
		if (result == Result.Ok){
			state = State.Clearing;
		}
		return result;// TODO need better request/response/event model
	}

	final public Result playback(String[] prompt, String terminators) {
		if (state != State.Idle) {
			log.warn(String.format("Channel (%s), playback, invalid state(%s)",
					this, state));
			return Result.ChannelInvalidState;
		}
		prompt = PromptsUtils.prepend(prompt, baseDirectory, "/sounds/",
				language.toString(), "/");

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
		prompt = PromptsUtils.prepend(prompt, baseDirectory, "/sounds/",
				language.toString(), "/");

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
		if (result == Result.Ok){
			state = State.Answering;
		}
		return result;// TODO need better request/response/event model
	}

	final public Result joinConference(String conferenceId, boolean moderator,
			boolean startMuted, boolean startDeaf) { // TODO add more parameters
		if (state != State.Idle) {
			log.warn(String.format(
					"Channel(%s), joinConference invalid state(%s)", this,
					state));
			return Result.ChannelInvalidState;
		}
		
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
		prompt = PromptsUtils.prepend(prompt, baseDirectory, "/sounds/",
				language.toString(), "/");
		state = State.MediaBusy;
		Result result = internalPlayAndGetDigits(max, prompt, timeout,
				terminators, true);
		if (result != Result.Ok) {
			state = State.Idle;
		}
		return result;

	}
	
	public abstract Result internalConferenceMute(String conferenceId, String memberId, boolean mute);
	
	final public Result conferenceMute(boolean mute){
		if (state != State.Conferenced){
			log.warn(String.format("Channel (%s), conferenceMute(%b), invalid state (%s)", this, mute, state));
			return Result.ChannelInvalidState;
		}
		return internalConferenceMute(conferenceId, memberId, mute);
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
		log.trace(String.format("Channel(%s) dialing %s", this, address));
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
		case QueueLeft:
			state = State.Idle;
			break;
		case Answered:
			state = State.Idle;
			setAnswerTime(new DateTime());
			sendInterimUpdate();
			interimUpdateTimer = Timers.getInstance().startTimer(
					this,
					SystemConfig.getInstance().serverDefinition
							.getInterimUpdate() * 1000, false,
					TimersDefs.InterimUpdateTimer);

			break;
		case InboundAlerting: {
			InboundAlertingEvent ie = (InboundAlertingEvent) e;
			ie.setCalledIdNumber(getChannelData().getCalledNumber());
			ie.setCallerIdNumber(getChannelData().getCallerIdNumber());
			state = State.InboundAlerting;
			callOrigin = CallOrigin.Inbound;
			setupTime = new DateTime();			
		}
			break;
		case Hangup: {
			HangupEvent he = (HangupEvent) e;
			hangupTime = new DateTime();
			h323DisconnectCause = he.getHangupCause();
			stopTimers();			
		}
			break;
		case OutboundAlerting:
			state = State.OutboundAlerting;
			callOrigin = CallOrigin.Outbound;
			setupTime = new DateTime();			
			break;
		case QueueJoined:
			state = State.Queued;
			break;
		case ConferenceJoined: {
			ConferenceJoinedEvent cje = (ConferenceJoinedEvent) e;
			this.conferenceId = cje.getConferenceId();
			this.memberId = cje.getParticipantId();
			state = State.Conferenced;
		}
			break;
		case ConferenceLeft: {
			this.conferenceId = null;
			this.memberId = null;
			state = State.Idle;
		}
			break;
		}

		EventsManager.getInstance().onEvent(e);
		
		if (script != null){			
			script.onEvent(e);
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
			setScript(null);
			_switch.removeChannel(this);
		}
		return false;
	}

	public Switch get_switch() {
		return _switch;
	}

	public void set_switch(Switch switch1) {
		_switch = switch1;
	}

	private void stopTimers() {
		Timers.getInstance().stopTimer(hangupTimer);
		Timers.getInstance().stopTimer(interimUpdateTimer);

	}

	public DateTime getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(DateTime answerTime) {
		this.answerTime = answerTime;
	}

	@Override
	public String toString() {
		return "Channel ["
				+ (callOrigin != null ? "callOrigin=" + callOrigin + ", " : "")
				+ (id != null ? "id=" + id + ", " : "")
				+ (getAccountCode() != null ? "getAccountCode()="
						+ getAccountCode() + ", " : "")
				+ (getLanguage() != null ? "getLanguage()=" + getLanguage()
						+ ", " : "")
				+ (getServiceNumber() != null ? "getServiceNumber()="
						+ getServiceNumber() + ", " : "")
				+ (getState() != null ? "getState()=" + getState() + ", " : "")
				+ (getUserName() != null ? "getUserName()=" + getUserName()
						: "") + "]";
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

	@Override
	public boolean onTimer(Object data) {
		if (data instanceof TimersDefs) {
			TimersDefs td = (TimersDefs) data;
			switch (td) {
			case HangupTimer:
				hangup(DisconnectCode.Normal);
				break;
			case InterimUpdateTimer:
				sendInterimUpdate();
				return false; // Don't remove the timer
			}
		}
		return true;
	}

	private void sendInterimUpdate() {
		RadiusServers.getInstance().accountingInterimUpdate(this);
	}

	public String getAccountCode() {
		return getChannelData().getAccountCode();
	}

	public void putMessage(Object message) {
		messageHandler.putMessage(message);
	}

	abstract protected void processNativeEvent(Object event);

	synchronized protected void processRequest(Request request) {
		// TODO send response events

		log.trace(String.format("processRequest{%s}", request));
		switch (request.getType()) {
		case HangupRequest: {
			HangupRequest hr = (HangupRequest) request;
			Result result = hangup(hr.getDisconnectCode());			
		}
			break;
		case AnswerRequest: {
			answer();
		}
			break;
		case ParticipantMuteRequest:
		{
			ParticipantMuteRequest pmr = (ParticipantMuteRequest) request;
			conferenceMute(pmr.isMute());			
		}
		break;
		case DialRequest:
		{
			DialRequest dialRequest = (DialRequest) request;
			dial(dialRequest.getDestination(), dialRequest.getTimeout());
		}
			break;
		}
	}
}
