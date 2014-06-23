package com.admtel.telephonyserver.asterisk;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.joda.time.DateTime;

import com.admtel.telephonyserver.acd.AcdManager;
import com.admtel.telephonyserver.asterisk.commands.ASTCommand;
import com.admtel.telephonyserver.asterisk.commands.ASTDialCommand;
import com.admtel.telephonyserver.asterisk.commands.ASTMeetmeMuteCommand;
import com.admtel.telephonyserver.asterisk.commands.ASTMeetmeUnmuteCommand;
import com.admtel.telephonyserver.asterisk.commands.ASTQueueCommand;
import com.admtel.telephonyserver.asterisk.commands.ASTSetVariableCommand;
import com.admtel.telephonyserver.asterisk.events.ASTAgiExecEvent;
import com.admtel.telephonyserver.asterisk.events.ASTAsyncAgiEvent;
import com.admtel.telephonyserver.asterisk.events.ASTChannelState;
import com.admtel.telephonyserver.asterisk.events.ASTDialEvent;
import com.admtel.telephonyserver.asterisk.events.ASTDtmfEvent;
import com.admtel.telephonyserver.asterisk.events.ASTEvent;
import com.admtel.telephonyserver.asterisk.events.ASTHangupEvent;
import com.admtel.telephonyserver.asterisk.events.ASTJoinEvent;
import com.admtel.telephonyserver.asterisk.events.ASTLeaveEvent;
import com.admtel.telephonyserver.asterisk.events.ASTMeetmeJoinEvent;
import com.admtel.telephonyserver.asterisk.events.ASTMeetmeLeaveEvent;
import com.admtel.telephonyserver.asterisk.events.ASTMeetmeMuteEvent;
import com.admtel.telephonyserver.asterisk.events.ASTMeetmeTalkingEvent;
import com.admtel.telephonyserver.asterisk.events.ASTNewCalleridEvent;
import com.admtel.telephonyserver.asterisk.events.ASTNewChannelEvent;
import com.admtel.telephonyserver.asterisk.events.ASTNewStateEvent;
import com.admtel.telephonyserver.asterisk.events.ASTEvent.EventType;
import com.admtel.telephonyserver.asterisk.events.ASTVarSetEvent;
import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.events.AcdQueueJoinedEvent;
import com.admtel.telephonyserver.events.ConnectedEvent;
import com.admtel.telephonyserver.events.ConferenceJoinedEvent;
import com.admtel.telephonyserver.events.ConferenceLeftEvent;
import com.admtel.telephonyserver.events.ConferenceMutedEvent;
import com.admtel.telephonyserver.events.ConferenceTalkEvent;
import com.admtel.telephonyserver.events.DestroyEvent;
import com.admtel.telephonyserver.events.DialFailedEvent;
import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.DialStatus;
import com.admtel.telephonyserver.events.DisconnectCode;
import com.admtel.telephonyserver.events.DisconnectedEvent;
import com.admtel.telephonyserver.events.AlertingEvent;
import com.admtel.telephonyserver.events.OfferedEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsEndedEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsStartedEvent;
import com.admtel.telephonyserver.events.PlaybackEndedEvent;
import com.admtel.telephonyserver.events.PlaybackStartedEvent;
import com.admtel.telephonyserver.events.QueueJoinedEvent;
import com.admtel.telephonyserver.events.QueueLeftEvent;
import com.admtel.telephonyserver.freeswitch.FSChannel;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.utils.SipParser;
import com.admtel.telephonyserver.utils.UriRecord;
import com.admtel.telephonyserver.core.CallOrigin;
import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.SigProtocol;
import com.admtel.telephonyserver.core.ConferenceManager;
import com.admtel.telephonyserver.core.Result;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.core.ScriptManager;
import com.admtel.telephonyserver.core.Switch;
import com.admtel.telephonyserver.core.Timers;

public class ASTChannel extends Channel {

	private static Logger log = Logger.getLogger(ASTChannel.class);

	private class PlayingState extends State {

		List<String> prompts = new ArrayList<String>();
		String terminators = "";
		int playedPromptCount = 0;
		String interruptingDigit = "";

		public PlayingState(String[] prompts, String terminators) {
			for (int i = 0; i < prompts.length; i++) {
				this.prompts.add(prompts[i]);
			}
			this.terminators = terminators;
			execute();
		}

		public PlayingState(String prompt, String terminators) {
			prompts.add(prompt);
			this.terminators = terminators;
			execute();
		}

		private Result execute() {
			playedPromptCount = 0;
			if (terminators == null || terminators.trim().isEmpty()) {
				terminators = "X";
			}

			String actionId = getId() + "___StreamFile";
			ASTChannel.this.session.write(String.format(
					"Action: AGI\nChannel: %s\nCommand: STREAM FILE %s %s\nActionId: %s\nCommandID: %s", getId(),
					prompts.get(playedPromptCount), terminators, actionId, actionId));
			return Result.Ok;
		}

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(ASTEvent astEvent) {

			switch (astEvent.getEventType()) {
			case AgiExec: {
				ASTAgiExecEvent aee = (ASTAgiExecEvent) astEvent;
				String command = aee.getValue("Command");
				if (command == null || command.indexOf("STREAM FILE") == -1) {
					return;
				}
				if (aee.isStart()) { // At this state, we only get the AgiExec
					// for the stream file command
					if (playedPromptCount == 0) {
						ASTChannel.this.onEvent(new PlaybackStartedEvent(ASTChannel.this));
					}
				} else {
					// playback ended
					playedPromptCount++;
					if (playedPromptCount == prompts.size() || !interruptingDigit.isEmpty()) {
						if (interruptingDigit.isEmpty()) {
							ASTChannel.this.onEvent(new PlaybackEndedEvent(ASTChannel.this, interruptingDigit, ""));
						} else {
							ASTChannel.this.onEvent(new PlaybackEndedEvent(ASTChannel.this, interruptingDigit, prompts
									.get(playedPromptCount - 1)));
						}
					} else {
						String actionId = getId() + "___StreamFile";
						ASTChannel.this.session.write(String.format(
								"Action: AGI\nChannel: %s\nCommand: STREAM FILE %s %s\nActionId: %s\nCommandID: %s",
								getId(), prompts.get(playedPromptCount), terminators, actionId, actionId));
					}
				}
			}
				break;
			case Dtmf: {
				ASTDtmfEvent dtmfEvent = (ASTDtmfEvent) astEvent;
				if (dtmfEvent.isBegin()) {
					if (terminators.indexOf(dtmfEvent.getDigit()) != -1) {
						this.interruptingDigit = dtmfEvent.getDigit();
					}
				}
			}
				break;
			}
		}
	}

	private abstract class State {
		public abstract void processEvent(ASTEvent astEvent);

		public abstract boolean onTimer(Object data);

		Result result;

		public void setResult(Result result) {
			this.result = result;
		}

		public Result getResult() {
			return this.result;
		}
	}

	private class IdleState extends State {

		@Override
		public void processEvent(ASTEvent astEvent) {
			switch (astEvent.getEventType()) {
			case NewState: {
				ASTNewStateEvent nse = (ASTNewStateEvent) astEvent;
				switch (nse.getChannelState()) {
				case Ring:
					break;
				}
			}
				break;
			}
		}

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	private class JoinConferenceState extends State {
		String conferenceId;
		boolean moderator;
		boolean muted;
		boolean deaf;

		public JoinConferenceState(String conferenceId, boolean moderator, boolean muted, boolean deaf) {

			this.conferenceId = conferenceId;
			this.moderator = moderator;
			this.muted = muted;
			this.deaf = deaf;
			String parameters = "dT";// Dynamically create the conference with
			// talked detection T
			if (moderator) {
				parameters += "a";
			}
			if (muted) {
				parameters += "m";
			}
			if (deaf) {
				parameters += "t";
			}

			String command = String.format("%s,%s,", conferenceId, parameters);
			String actionId = getId() + "___MeetMe";

			ASTChannel.this.session.write(String.format(
					"Action: AGI\nChannel: %s\nCommand: EXEC MeetMe %s\nActionId: %s\nCommandID: %s", getId(), command,
					actionId, actionId));

		}

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(ASTEvent astEvent) {
			switch (astEvent.getEventType()) {
			case MeetmeJoin: {
				ASTMeetmeJoinEvent je = (ASTMeetmeJoinEvent) astEvent;
				ASTChannel.this.onEvent(new ConferenceJoinedEvent(ASTChannel.this, je.getMeetme(), je.getUsernum(),
						moderator, muted, deaf));
			}
				break;
			case MeetmeLeave: {
				ASTMeetmeLeaveEvent mle = (ASTMeetmeLeaveEvent) astEvent;
				ASTChannel.this.onEvent(new ConferenceLeftEvent(ASTChannel.this, mle.getMeetme(), mle.getUsernum()));
			}
				break;
			case MeetmeTalking: {
				ASTMeetmeTalkingEvent mte = (ASTMeetmeTalkingEvent) astEvent;
				ASTChannel.this.onEvent(new ConferenceTalkEvent(ASTChannel.this, mte.getMeetme(), mte.getUsernum(), mte
						.isOn()));
			}
				break;
			case MeetmeMute: {
				ASTMeetmeMuteEvent mme = (ASTMeetmeMuteEvent) astEvent;
				ASTChannel.this.onEvent(new ConferenceMutedEvent(ASTChannel.this, mme.getMeetme(), mme.getUsernum(),
						mme.isMuted()));
			}
				break;
			}

		}

	}

	private class NullState extends State {

		@Override
		public void processEvent(ASTEvent astEvent) {
			switch (astEvent.getEventType()) {
			case Dial: {
				ASTDialEvent dialEvent = (ASTDialEvent) astEvent;
				if (dialEvent.isBegin()) {

					Channel peerChannel = ASTChannel.this.getSwitch().getChannel(dialEvent.getPeerChannel());
					if (peerChannel != null && peerChannel.getScript() != null) {
						peerChannel.getScript().addChannel(ASTChannel.this);
					}
					UriRecord uriRecord = SipParser.parseDialString(dialEvent.getDialString());
					ASTChannel.this.setCalledStationId(uriRecord.username);
					internalState = new AlertingState();
					onEvent(new DialStartedEvent(peerChannel, ASTChannel.this));
					onEvent(new AlertingEvent(ASTChannel.this));

				} else {
					if (dialEvent.getDialStatus() != DialStatus.Answer) {
						onEvent(new DialFailedEvent(ASTChannel.this, dialEvent.getDialStatus()));
					}
				}

			}
				break;

			case NewChannel: {
				ASTNewChannelEvent nce = (ASTNewChannelEvent) astEvent;
				getChannelData().setCallerIdNumber(nce.getCallerIdNum());
				getChannelData().setUserName(nce.getUserName());
				getChannelData().setServiceNumber(nce.getExten());
				setAccountCode(nce.getAccountCode());
				getChannelData().setCalledNumber(nce.getExten());
				switch (nce.getChannelState()) {
				case Ring:
					internalState = new OfferedState();
					break;
				case Ringing:
					internalState = new AlertingState();
					break;
				case Answer:
					ASTChannel.this.onEvent(new ConnectedEvent(ASTChannel.this));
					internalState = new IdleState();
					break;
				}
			}
				break;
			case NewState: {
				ASTNewStateEvent nse = (ASTNewStateEvent) astEvent;
				switch (nse.getChannelState()) {
				case Ring:
					internalState = new OfferedState();
					break;
				case Ringing:
					internalState = new AlertingState();
					break;
				case Answer:
					ASTChannel.this.onEvent(new ConnectedEvent(ASTChannel.this));
					internalState = new IdleState();
					break;
				}

			}
				break;
			}
		}

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	private class OfferedState extends State {

		ASTVariableFetcher variableFetcher = new ASTVariableFetcher(ASTChannel.this);

		public OfferedState() {
			ASTChannel.this.setCallOrigin(CallOrigin.Inbound);
		}

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(ASTEvent astEvent) {
			switch (astEvent.getEventType()) {
			case AsyncAgi: {
				ASTAsyncAgiEvent agiEvent = (ASTAsyncAgiEvent) astEvent;
				if (agiEvent.isStartAgi()) {
					ASTChannel.this.getChannelData().addVariable("context", agiEvent.getValue("agi_context"));
					// Create script
					if (ScriptManager.getInstance().createScript(ASTChannel.this) != null) {

						if (ASTChannel.this.getAcctUniqueSessionId() == null) {
							ASTChannel.this.setAcctUniqueSessionId(UUID.randomUUID().toString());
						}

						ASTChannel.this.onEvent(new OfferedEvent(ASTChannel.this));
					} else {
						ASTChannel.this.hangup(DisconnectCode.Normal);
					}
				}
			}

				break;

			case NewState: {
				ASTNewStateEvent nse = (ASTNewStateEvent) astEvent;
				switch (nse.getChannelState()) {
				case Answer:
					internalState = new IdleState();
					ASTChannel.this.onEvent(new ConnectedEvent(ASTChannel.this));
					break;
				}

			}
				break;
			}
		}
	}

	private class AlertingState extends State {

		ASTVariableFetcher variableFetcher = new ASTVariableFetcher(ASTChannel.this);

		public AlertingState() {
			ASTChannel.this.setCallOrigin(CallOrigin.Outbound);
		}

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(ASTEvent astEvent) {

			switch (astEvent.getEventType()) {

			case NewState: {
				ASTNewStateEvent nse = (ASTNewStateEvent) astEvent;
				switch (nse.getChannelState()) {
				case Answer:
					internalState = new IdleState();
					ASTChannel.this.onEvent(new ConnectedEvent(ASTChannel.this));
					break;
				}

			}
				break;

			}
		}

	}

	private class PlayingAndGettingDigitsState extends State {

		int max;
		long timeout;
		String terminators;
		boolean interruptPlay;
		int playedPromptCount = 0;

		final static int INIT = 0;
		final static int PLAYING = 1;
		final static int GETTING_DIGITS = 2;
		final static int ENDED = 3;

		int substate = INIT;

		List<String> prompts = new ArrayList<String>();

		Timer maxDtmfTimer = null;
		String digits = "";

		public PlayingAndGettingDigitsState(int max, String prompt, long timeout, String terminators,
				boolean interruptPlay) {
			this.prompts.add(prompt);
			this.max = max;
			this.timeout = timeout;
			this.terminators = terminators;
			this.interruptPlay = interruptPlay;
			playedPromptCount = 0;
			execute(); // TODO check return result
		}

		public PlayingAndGettingDigitsState(int max, String[] prompts, long timeout, String terminators,
				boolean interruptPlay) {
			if (prompts != null && prompts.length > 0) {
				for (int i = 0; i < prompts.length; i++) {
					this.prompts.add(prompts[i]);
				}
			}
			this.max = max;
			this.timeout = timeout;
			this.terminators = terminators;
			this.interruptPlay = interruptPlay;
			playedPromptCount = 0;
			execute();// TODO check return result
		}

		@Override
		public void processEvent(ASTEvent astEvent) {
			switch (astEvent.getEventType()) {
			case Dtmf: {
				ASTDtmfEvent dtmfEvent = (ASTDtmfEvent) astEvent;
				if (dtmfEvent.isEnd()) {
					switch (substate) {
					case PLAYING:
						if (this.interruptPlay) {
							this.digits += dtmfEvent.getDigit();

							if (this.max == 1) {
								String termChar = "";
								if (this.terminators.indexOf(this.digits) != -1) {
									termChar = this.digits;
									this.digits = "";
								}
								PlayAndGetDigitsEndedEvent pe = null;

								pe = new PlayAndGetDigitsEndedEvent(ASTChannel.this, this.digits);
								pe.setTerminatingDigit(termChar);
								if (this.playedPromptCount < prompts.size()) {
									pe.setInterruptedFile(prompts.get(this.playedPromptCount));
								}

								substate = ENDED;
								ASTChannel.this.internalState = new IdleState();
								ASTChannel.this.onEvent(pe);

							} else {
								String termChar = "";
								if (this.terminators.indexOf(this.digits) != -1) {
									termChar = this.digits;
									this.digits = "";
									PlayAndGetDigitsEndedEvent pe = null;

									pe = new PlayAndGetDigitsEndedEvent(ASTChannel.this, this.digits);
									pe.setTerminatingDigit(termChar);
									if (this.playedPromptCount < prompts.size()) {
										pe.setInterruptedFile(prompts.get(this.playedPromptCount));
									}
									substate = ENDED;
									ASTChannel.this.internalState = new IdleState();
									ASTChannel.this.onEvent(pe);

								} else {
									substate = GETTING_DIGITS;
									this.maxDtmfTimer = Timers.getInstance().startTimer(ASTChannel.this, timeout, true,
											this);
								}

							}
						}
						break;
					case GETTING_DIGITS: {

						String termChar = "";
						if (terminators.indexOf(dtmfEvent.getDigit()) != -1) {
							termChar = dtmfEvent.getDigit();
						} else {
							this.digits += dtmfEvent.getDigit();
						}
						if (termChar.length() > 0 || this.digits.length() == this.max) {
							PlayAndGetDigitsEndedEvent pe = new PlayAndGetDigitsEndedEvent(ASTChannel.this, this.digits);
							pe.setTerminatingDigit(termChar);
							if (this.playedPromptCount < prompts.size()) {
								pe.setInterruptedFile(prompts.get(this.playedPromptCount));
							}

							substate = ENDED;
							ASTChannel.this.internalState = new IdleState();
							ASTChannel.this.onEvent(pe);
							Timers.getInstance().stopTimer(maxDtmfTimer);
						}
					}
						break;
					}
				}
			}
				break;
			case AgiExec: {
				ASTAgiExecEvent aee = (ASTAgiExecEvent) astEvent;
				String command = aee.getValue("Command");
				if (command == null || command.indexOf("STREAM FILE") == -1) {
					return;
				}
				if (aee.isStart()) { // At this state, we only get the AgiExec
					// for the stream file command
					if (playedPromptCount == 0) {
						ASTChannel.this.onEvent(new PlayAndGetDigitsStartedEvent(ASTChannel.this));
						return;
					}
				} else { // end event
					if (substate == PLAYING) { // we're still playing
						playedPromptCount++;
						if (playedPromptCount < prompts.size()) {
							String interruptDigits = "_";
							if (interruptPlay) {
								interruptDigits = "1234567890*#";
							}
							String actionId = getId() + "___StreamFile";
							ASTChannel.this.session
									.write(String
											.format("Action: AGI\nChannel: %s\nCommand: STREAM FILE %s %s\nActionId: %s\nCommandID: %s",
													getId(), prompts.get(playedPromptCount), interruptDigits, actionId,
													actionId));

						} else {// that was the last prompt to play
							substate = GETTING_DIGITS;
							this.maxDtmfTimer = Timers.getInstance().startTimer(ASTChannel.this, timeout, true, this);
						}
					}
				}

			}
				break;
			}
		}

		private Result execute() {
			String interruptDigits = "_";
			if (interruptPlay) {
				interruptDigits = "1234567890*#";
			}
			if (timeout <= 0) {
				return Result.InvalidParameters;
			}
			if (prompts.size() > 0) {
				String actionId = getId() + "___StreamFile";
				ASTChannel.this.session.write(String.format(
						"Action: AGI\nChannel: %s\nCommand: STREAM FILE %s %s\nActionId: %s\nCommandID: %s", getId(),
						prompts.get(playedPromptCount), interruptDigits, actionId, actionId));
				substate = PLAYING;

			} else {
				// Send started event
				ASTChannel.this.onEvent(new PlayAndGetDigitsStartedEvent(ASTChannel.this));
				// Start the timer
				substate = GETTING_DIGITS;
				this.maxDtmfTimer = Timers.getInstance().startTimer(ASTChannel.this, timeout, true, this);
			}

			return Result.Ok;
		}

		@Override
		public boolean onTimer(Object data) {
			if (data == this) {// it is our own timer
				if (substate == GETTING_DIGITS) {
					PlayAndGetDigitsEndedEvent pe = new PlayAndGetDigitsEndedEvent(ASTChannel.this, this.digits);
					substate = ENDED;
					ASTChannel.this.internalState = new IdleState();
					ASTChannel.this.onEvent(pe);
				}
			}
			return true; // stop the timer, eventhought it is a one shot timer,
			// it doesn't hurt to make sure it is removed
		}
	}

	private class QueueState extends State {

		private String queueName;
		private boolean isAgent;

		public QueueState(String queueName, boolean isAgent) {
			this.queueName = queueName;
			this.isAgent = isAgent;
			ASTQueueCommand queueCmd = new ASTQueueCommand(ASTChannel.this, queueName);
			session.write(queueCmd);
			result = Result.Ok;
		}

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(ASTEvent astEvent) {
			switch (astEvent.getEventType()) {
			case Join: {
				ASTJoinEvent je = (ASTJoinEvent) astEvent;
				ASTChannel.this.onEvent(new QueueJoinedEvent(ASTChannel.this, je.getQueue(), isAgent));
			}
				break;
			case Leave: {
				ASTLeaveEvent le = (ASTLeaveEvent) astEvent;
				ASTChannel.this.onEvent(new QueueLeftEvent(ASTChannel.this, le.getQueue(), isAgent, "Uknown"));
			}
				break;
			}

		}

	}

	private class AcdQueueState extends State {

		@Override
		public void processEvent(ASTEvent astEvent) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	// END STATES LOGIC
	// ////////////////////////////////////////////////////////////////////////////////////////

	protected State internalState = new NullState();
	protected IoSession session;

	protected SigProtocol channelProtocol = SigProtocol.Unknown;

	synchronized protected void processNativeEvent(Object event) {

		log.debug(String.format("Channel(%s)\nEvent (%s)\n CallState (%s)\n InternalState(%s)", this, event,
				getCallState(), internalState.getClass().getSimpleName()));
		if (event instanceof ASTEvent) {
			ASTEvent astEvent = (ASTEvent) event;

			switch (astEvent.getEventType()) {
			case Hangup: {
				ASTHangupEvent asthe = (ASTHangupEvent) astEvent;
				DisconnectedEvent he = new DisconnectedEvent(ASTChannel.this, DisconnectCode.get(asthe.getCause()));

				ASTChannel.this.onEvent(he);
				ASTChannel.this.onEvent(new DestroyEvent(this));
			}
				break;
			case VarSet: {
				ASTVarSetEvent vse = (ASTVarSetEvent) astEvent;
				String variableName = vse.getName();
				if (variableName.equals("SIPURI")) {
					UriRecord uriRecord = SipParser.parseUri(vse.getValue());
					setLoginIP(uriRecord.host);
				}
			}
				break;
			}
			if (internalState != null) {
				internalState.processEvent(astEvent);
			}
		}
	}

	public ASTChannel(Switch _switch, String id, IoSession session) {
		super(_switch, id);
		this.session = session;
		if (id.toLowerCase().startsWith("sip")) {
			channelProtocol = SigProtocol.SIP;
		} else if (id.toLowerCase().startsWith("iax2")) {
			channelProtocol = SigProtocol.IAX2;
		}
	}

	@Override
	public void setCallingStationId(String callingStationId) {
		super.setCallingStationId(callingStationId);
		ASTSetVariableCommand cmd = new ASTSetVariableCommand(this, "CALLERID(num)", callingStationId);
		session.write(cmd.toString());
	}

	@Override
	public Result internalAnswer() {
		ASTChannel.this.session.write(String.format(
				"Action: AGI\nChannel: %s\nCommand: Answer\nActionId: %s\nCommandId: %s", getId(), getId()
						+ "___Answer", getId() + "___Answer"));
		return Result.Ok;
	}

	@Override
	public Result internalHangup(Integer cause) {

		// TODO Hangup cause for asterisk is not working

		ASTChannel.this.session.write(String.format("Action: Hangup\nChannel: %s\nCause: %d", getId(), cause));

		/*
		 * String actionId = getId() + "___Hangup"; ASTChannel.this.session
		 * .write(String .format(
		 * "Action: AGI\nChannel: %s\nCommand: EXEC Hangup\nActionId: %s\nCommandID: %s"
		 * , getId(), actionId, actionId));
		 */
		return Result.Ok;
	}

	@Override
	public Result internalDial(String address, long timeout, boolean secure) {
		if (address != null && address.length() > 0) {
			ASTDialCommand dialCmd = new ASTDialCommand(ASTChannel.this, address, timeout);
			log.trace(String.format("Channel (%s) dialCmd %s", this, dialCmd));
			session.write(dialCmd);
		} else {
			log.warn(String.format("%s, invalid dial string %s", this.getId(), address));
			return Result.InvalidParameters;
		}
		return Result.Ok;

	}

	@Override
	public Result internalPlayAndGetDigits(int max, String prompt, long timeout, String terminators,
			boolean interruptPlay) {
		internalState = new PlayingAndGettingDigitsState(max, prompt, timeout, terminators, interruptPlay);
		return Result.Ok;
	}

	@Override
	public Result internalPlayAndGetDigits(int max, String[] prompt, long timeout, String terminators,
			boolean interruptPlay) {
		internalState = new PlayingAndGettingDigitsState(max, prompt, timeout, terminators, interruptPlay);
		return Result.Ok;
	}

	@Override
	public Result internalPlayback(String[] prompt, String terminators) {
		internalState = new PlayingState(prompt, terminators);

		return Result.Ok;
	}

	@Override
	public Result internalPlayback(String prompt, String terminators) {
		internalState = new PlayingState(prompt, terminators);
		return Result.Ok;
	}

	@Override
	synchronized public boolean onTimer(Object data) {
		boolean result = super.onTimer(data);
		log.debug("Got timer .... " + data);
		if (data instanceof State) {
			return ((State) data).onTimer(data);
		}
		return result;
	}

	@Override
	public Result internalJoinConference(String conferenceId, boolean moderator, boolean startMuted, boolean startDeaf) {
		internalState = new JoinConferenceState(conferenceId, moderator, startMuted, startDeaf);// TODO
																								// check
																								// state
		return Result.Ok;
	}

	public String getSwitchId() {
		return null;
	}

	public String getContext() {
		return this.getChannelData().get("context");
	}

	@Override
	public Result internalQueue(String queueName, boolean isAgent) {
		internalState = new QueueState(queueName, isAgent);
		return Result.Ok;
	}

	@Override
	public Result internalConferenceMute(String conferenceId, String memberId, boolean mute) {
		ASTCommand cmd = null;
		if (mute) {
			cmd = new ASTMeetmeMuteCommand(this, conferenceId, memberId);
		} else {
			cmd = new ASTMeetmeUnmuteCommand(this, conferenceId, memberId);
		}
		session.write(cmd);
		return Result.Ok;
	}

	@Override
	public Result internalAcdQueue(String queueName) {
		Result result = AcdManager.getInstance().queueChannel(queueName, ASTChannel.this.getUniqueId(),
				ASTChannel.this.getSetupTime().toDate(), 0);
		if (result == Result.Ok) {// TODO,
			// priority
			onEvent(new AcdQueueJoinedEvent(this, queueName, false));
		}

		return result;
	}

	@Override
	public Result internalConferenceDeaf(String conferenceId, String memberId, boolean deaf) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "ASTChannel [" + (internalState != null ? "internalState=" + internalState + ", " : "")
				+ (super.toString() != null ? "toString()=" + super.toString() : "") + "]";
	}

}