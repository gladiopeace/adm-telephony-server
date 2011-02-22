package com.admtel.telephonyserver.core;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.admtel.telephonyserver.acd.AcdManager;
import com.admtel.telephonyserver.config.SystemConfig;
import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.events.AcdQueueBridgeFailedEvent;
import com.admtel.telephonyserver.events.ConferenceJoinedEvent;
import com.admtel.telephonyserver.events.ConferenceLeftEvent;
import com.admtel.telephonyserver.events.DialFailedEvent;
import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.DialStatus;
import com.admtel.telephonyserver.events.DisconnectCode;
import com.admtel.telephonyserver.events.DtmfEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.DisconnectedEvent;
import com.admtel.telephonyserver.events.AlertingEvent;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.freeswitch.FSChannel;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.radius.RadiusServers;
import com.admtel.telephonyserver.registrar.UserLocation;
import com.admtel.telephonyserver.requests.AnswerRequest;
import com.admtel.telephonyserver.requests.DialRequest;
import com.admtel.telephonyserver.requests.HangupRequest;
import com.admtel.telephonyserver.requests.JoinConferenceRequest;
import com.admtel.telephonyserver.requests.ParticipantMuteRequest;
import com.admtel.telephonyserver.requests.Request;
import com.admtel.telephonyserver.utils.AdmUtils;
import com.admtel.telephonyserver.utils.PromptsUtils;

public abstract class Channel implements TimerNotifiable {

	public static Logger log = Logger.getLogger(Channel.class);

	public enum CallState {
		Null, Idle, Offered, Alerting, Accepted, Connected, Dialing, Disconnected, Dropped, Conferenced, AcdQueued, Queued,
	}

	public enum MediaState {
		PlayAndGetDigits, Idle, Playback,
	}

	private enum TimersDefs {
		HangupTimer, InterimUpdateTimer
	}

	Timer hangupTimer;
	Timer interimUpdateTimer;

	private List<EventListener> listeners = new CopyOnWriteArrayList<EventListener>();

	private CallState callState = CallState.Idle;
	private MediaState mediaState = MediaState.Idle;
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
	private Result lastResult = Result.Ok;
	private Channel otherChannel = null;

	public Channel getOtherChannel() {
		return otherChannel;
	}

	public void setOtherChannel(Channel otherChannel) {
		this.otherChannel = otherChannel;
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

	final public Result acdQueue(String queueName) { // TODO add more parameters
		Result result = internalAcdQueue(queueName);
		return result;
	}

	public abstract Result internalAcdQueue(String queueName);

	private boolean isConnected() {
		return getCallState() == CallState.Connected;
	}

	private boolean isMediaActive() {
		return mediaState != MediaState.Idle;
	}

	final public Result playAndGetDigits(int max, String prompt, long timeout,
			String terminators) {
		log.trace(String.format("[%s] - playAndGetDigits(%d,%s,%d,%s)", this,
				max, prompt, timeout, terminators));
		if (!isConnected()) {
			log.warn(String.format(
					"[%s], playAndGetDigits, invalid call state", this));
			lastResult = Result.ChannelInvalidCallState;
			return lastResult;
		}

		if (isMediaActive()) {
			log.warn(String.format(
					"[%s], playAndGetDigits, invalid media state", this));

			lastResult = Result.ChannelInvalidMediaState;
			return lastResult;
		}
		prompt = PromptsUtils.prepend(prompt, baseDirectory, "/sounds/",
				language.toString(), "/");
		lastResult = internalPlayAndGetDigits(max, prompt, timeout,
				terminators, true);
		if (lastResult == Result.Ok) {
			mediaState = MediaState.PlayAndGetDigits;
		}
		return lastResult;
	}

	final public Result playAndGetDigits(int max, String[] prompt,
			long timeout, String terminators) {
		log.trace(String.format("[%s] - playAndGetDigits(%d,%s,%d,%s)", this,
				max, prompt, timeout, terminators));
		if (!isConnected()) {
			log.warn(String.format(
					"[%s], playAndGetDigits, invalid call state", this));
			lastResult = Result.ChannelInvalidCallState;
			return lastResult;
		}

		if (isMediaActive()) {
			log.warn(String.format(
					"[%s], playAndGetDigits, invalid media state", this));

			lastResult = Result.ChannelInvalidMediaState;
			return lastResult;
		}
		prompt = PromptsUtils.prepend(prompt, baseDirectory, "/sounds/",
				language.toString(), "/");

		lastResult = internalPlayAndGetDigits(max, prompt, timeout,
				terminators, true);
		if (lastResult == Result.Ok) {
			mediaState = MediaState.PlayAndGetDigits;
		}
		return lastResult;

	}

	final public Result hangup(DisconnectCode disconnectCode) {

		log.trace(String.format("[%s] - hangup (%s)", this, disconnectCode));
		if (getCallState() == CallState.Connected
				|| getCallState() == CallState.Accepted
				|| getCallState() == CallState.Alerting
				|| getCallState() == CallState.Dialing
				|| getCallState() == CallState.Offered
				|| getCallState() == CallState.Conferenced) {
			lastResult = internalHangup(disconnectCode.ordinal());
			if (lastResult == Result.Ok) {
				setCallState(CallState.Dropped);
			}
		} else {
			log.warn(String.format("[%s] - hangup invalid call state", this));
			lastResult = Result.ChannelInvalidCallState;
		}
		return lastResult;
	}

	final public Result playback(String[] prompt, String terminators) {
		log.trace(String.format("[%s] - playback(%s,%s)", this, prompt,
				terminators));
		if (!isConnected()) {
			log.warn(String.format("[%s], playback, invalid call state", this));
			lastResult = Result.ChannelInvalidCallState;
			return lastResult;
		}

		if (isMediaActive()) {
			log.warn(String.format("[%s], playback, invalid media state", this));
			lastResult = Result.ChannelInvalidMediaState;
			return lastResult;
		}

		prompt = PromptsUtils.prepend(prompt, baseDirectory, "/sounds/",
				language.toString(), "/");

		lastResult = internalPlayback(prompt, terminators);
		if (lastResult == Result.Ok) {
			mediaState = MediaState.Playback;
		}
		return lastResult;
	}

	final public Result playback(String prompt, String terminators) {
		log.trace(String.format("[%s] - playback(%s,%s)", this, prompt,
				terminators));
		if (!isConnected()) {
			log.warn(String.format("[%s], playback, invalid call state", this));
			lastResult = Result.ChannelInvalidCallState;
			return lastResult;
		}

		if (isMediaActive()) {
			log.warn(String.format("[%s], playback, invalid media state", this));
			lastResult = Result.ChannelInvalidMediaState;
			return lastResult;
		}
		prompt = PromptsUtils.prepend(prompt, baseDirectory, "/sounds/",
				language.toString(), "/");

		lastResult = internalPlayback(prompt, terminators);
		if (lastResult == Result.Ok) {
			mediaState = MediaState.Playback;
		}
		return lastResult;

	}

	final public Result answer() {
		log.trace(String.format("[%s] - answer", this));

		if (getCallState() == CallState.Offered) {
			lastResult = internalAnswer();
		} else {
			log.warn(String.format("[%s] - answer invalid call state", this));
			lastResult = Result.ChannelInvalidCallState;
		}
		if (lastResult == Result.Ok) {
			setCallState(CallState.Accepted);
		}
		return lastResult;
	}

	final public Result joinConference(String conferenceId, boolean moderator,
			boolean startMuted, boolean startDeaf) { // TODO add more parameters

		log.trace(String.format("[%s] - joinConference (%s, %s, %s, %s)",
				this, conferenceId, moderator, startMuted, startDeaf));
		if (getCallState() == CallState.Connected) {
			lastResult = internalJoinConference(conferenceId, moderator,
					startMuted, startDeaf);
		} else {
			log.warn(String.format("[%s] - hangup invalid call state"));
			lastResult = Result.ChannelInvalidCallState;
		}
		if (lastResult == Result.Ok) {
			setCallState(CallState.Conferenced);
		}
		return lastResult;
	}

	public abstract Result internalJoinConference(String conferenceId,
			boolean moderator, boolean startMuted, boolean startDeaf);

	public abstract Result internalConferenceMute(String conferenceId,
			String memberId, boolean mute);

	final public Result conferenceMute(boolean mute) {
		log.trace(String.format("[%s] conferenceMute(%b)", this, mute));
		if (getCallState() != CallState.Conferenced) {
			log.warn(String.format("Channel (%s), invalid state", this));
			lastResult = Result.ChannelInvalidCallState;
			return lastResult;
		}
		lastResult = internalConferenceMute(conferenceId, memberId, mute);

		return lastResult;
	}

	final public Result dial(String address, long timeout) {

		log.trace(String.format("Channel(%s) dialing %s", this, address));

		if (getCallState() == CallState.Connected
				|| getCallState() == CallState.Accepted
				|| getCallState() == CallState.Offered
				|| getCallState() == CallState.AcdQueued) {
			String tAddress = address;
			if (address.startsWith("user:")) {
				tAddress = address.substring(5);
				UserLocation location = Registrar.getInstance().find(tAddress);
				if (location == null) {
					log.warn(String.format("Channel(%s) - User (%s) not found",
							this, address));
					lastResult = Result.UserNotFound;
					onEvent (new DialFailedEvent(this, DialStatus.InvalidNumber));
					return lastResult;
				} else {
					tAddress = location.getAddress(_switch);
				}
			}
			String translatedAddress = _switch.getAddressTranslator()
					.translate(AdmAddress.fromString(tAddress));
			if (translatedAddress == null) {
				log.warn(String.format(
						"Channel (%s) - invalid dial string (%s)", this,
						address));
				lastResult = Result.InvalidParameters;
				onEvent (new DialFailedEvent(this, DialStatus.InvalidNumber));
				return lastResult;
			}

			lastResult = internalDial(translatedAddress, timeout);
		} else {
			log.warn(String.format("Channel (%s), invalid state", this));
			lastResult = Result.ChannelInvalidCallState;
			onEvent (new DialFailedEvent(this, DialStatus.InvalidState));
		}
		return lastResult;
	}

	public Result getLastResult() {
		return lastResult;
	}

	public void setLastResult(Result lastResult) {
		this.lastResult = lastResult;
	}

	public boolean onEvent(Event e) {
		if (e == null)
			return true;
		log.trace(String.format("START : %s", e));
		switch (e.getEventType()) {
		case DTMF: {
			DtmfEvent event = (DtmfEvent) e;
			dtmfBuffer += event.getDigit();
		}
			break;
		case PlaybackEnded:
			setMediaState(MediaState.Idle);
			break;
		case PlayAndGetDigitsEnded:
			setMediaState(MediaState.Idle);
			break;
		case AnswerFailed:
			setCallState(CallState.Connected);
			break;
		case PlaybackFailed:
			setMediaState(MediaState.Idle);
			break;
		case PlayAndGetDigitsFailed:
			setMediaState(MediaState.Idle);
			break;
		case QueueLeft:
			setCallState(CallState.Connected);
			break;
		case Offered:
			setupTime = new DateTime();
			setCallState(CallState.Offered);
			break;
		case Connected:
			setCallState(CallState.Connected);
			setAnswerTime(new DateTime());
			sendInterimUpdate();
			interimUpdateTimer = Timers.getInstance().startTimer(
					this,
					SystemConfig.getInstance().serverDefinition
							.getInterimUpdate() * 1000, false,
					TimersDefs.InterimUpdateTimer);

			break;
		case AcdQueueJoined:
			setCallState(CallState.AcdQueued);
			break;
		case DialStarted: {
			DialStartedEvent dse = (DialStartedEvent) e;
			if (dse.getDialedChannel() != null
					&& dse.getDialedChannel().getCallOrigin() == CallOrigin.Outbound) {
				// TODO
				/*
				 * dse.getDialedChannel().getChannelData().setLoginIP(
				 * getChannelAddress());
				 */dse.getDialedChannel().setAcctUniqueSessionId(
						getAcctUniqueSessionId());
				dse.getDialedChannel().setUserName(getUserName());
				dse.getDialedChannel()
						.getChannelData()
						.setDestinationNumberIn(
								getChannelData().getCalledNumber());
				dse.getDialedChannel().getChannelData()
						.setRemoteIP(getLoginIP());
				dse.getDialedChannel().setOtherChannel(this);
			}
		}
			break;
		case Alerting: {
			AlertingEvent ie = (AlertingEvent) e;
			setCallState(CallState.Alerting);
			setupTime = new DateTime();
		}
			break;
		case Disconnected: {
			DisconnectedEvent he = (DisconnectedEvent) e;
			hangupTime = new DateTime();
			h323DisconnectCause = he.getDisconnectCode().toInteger();
			stopTimers();
		}
			break;
		case QueueJoined:
			setCallState(CallState.Queued);
			break;
		case ConferenceJoined: {
			ConferenceJoinedEvent cje = (ConferenceJoinedEvent) e;
			this.conferenceId = cje.getConferenceId();
			this.memberId = cje.getParticipantId();
			setCallState(CallState.Conferenced);
		}
			break;
		case ConferenceLeft: {
			this.conferenceId = null;
			this.memberId = null;
			setCallState(CallState.Connected);
		}
			break;
		}

		EventsManager.getInstance().onEvent(e);

		try {
			Iterator<EventListener> it = getListeners().iterator();
			while (it.hasNext()) {
				it.next().onEvent(e);
			}
		} catch (Exception ex) {
			log.fatal(AdmUtils.getStackTrace(ex));
		}
		if (e.getEventType() == EventType.Disconnected) {
			removeAllEventListeners();
			_switch.removeChannel(this);
		}
		log.trace(String.format("END : %s", e));
		return false;
	}

	public MediaState getMediaState() {
		return mediaState;
	}

	public void setMediaState(MediaState mediaState) {
		this.mediaState = mediaState;
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
		return String
				.format("\t\n\tuniqueId=%s\n\tcallState=%s\n\tmediaState=%s\n\tcallOrigin=%s",
						uniqueId, callState, mediaState, callOrigin);
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
		case ParticipantMuteRequest: {
			ParticipantMuteRequest pmr = (ParticipantMuteRequest) request;
			conferenceMute(pmr.isMute());
		}
			break;
		case DialRequest: {
			DialRequest dialRequest = (DialRequest) request;
			dial(dialRequest.getDestination(), dialRequest.getTimeout());
		}
			break;
		case JoinConferenceRequest:{
			JoinConferenceRequest jcr = (JoinConferenceRequest) request;
			joinConference(jcr.getConference(), jcr.isModerator(), jcr.isMuted(), jcr.isDeaf());
		}
		break;
		}
	}

	public void setCallState(CallState callState) {
		this.callState = callState;
	}

	public CallState getCallState() {
		return callState;
	}
}
