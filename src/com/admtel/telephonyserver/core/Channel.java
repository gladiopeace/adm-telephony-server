package com.admtel.telephonyserver.core;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.DtmfEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.InboundAlertingEvent;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.utils.AdmUtils;

public abstract class Channel {

	Logger log = Logger.getLogger(Channel.class);

	public enum State {
		Null, InboundAlerting, Idle, Clearing, Answering, OutboundAlerting, MediaBusy, Busy, Conferenced,
	}

	List<EventListener> listeners = new CopyOnWriteArrayList<EventListener>();

	protected State state = State.Idle;
	protected String id;
	protected String dtmfBuffer = "";
	protected Switch _switch;
	
	protected String uniqueId;

	protected ChannelData channelData = new ChannelData();
	protected Participant conferenceParticipant; //information about the channel when joined in a conference bridge

	public void addEventListener(EventListener listener) {
		listeners.add(listener);
	}

	public void removeEventListener(EventListener listener) {
		listeners.remove(listener);
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

	public abstract Result internalHangup(String cause);

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

	final public Result hangup(String cause) {
		Result result = internalHangup(cause);
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
		case Answered:
		case AnswerFailed:
		case PlaybackFailed:
		case PlayAndGetDigitsFailed:
			state = State.Idle;
			break;
		case InboundAlerting: {
			InboundAlertingEvent ie = (InboundAlertingEvent) e;
			state = State.InboundAlerting;

		}
			break;
		case OutboundAlerting:
			state = State.OutboundAlerting;
		}
		try {
			Iterator<EventListener> it = listeners.iterator();
			while (it.hasNext()) {
				it.next().onEvent(e);
			}
		} catch (Exception ex) {
			log.fatal(AdmUtils.getStackTrace(ex));
		}
		return false;
	}

	public String toString() {
		return String.format("Channel:%s:%s:%s", this.getClass()
				.getSimpleName(), this.state, this.id);
	}
}
