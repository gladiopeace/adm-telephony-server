package com.admtel.telephonyserver.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.mina.core.session.IoSession;
import org.joda.time.DateTime;

import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.events.AnsweredEvent;
import com.admtel.telephonyserver.events.ConferenceJoinedEvent;
import com.admtel.telephonyserver.events.ConferenceLeftEvent;
import com.admtel.telephonyserver.events.ConferenceTalkEvent;
import com.admtel.telephonyserver.events.DialFailedEvent;
import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;
import com.admtel.telephonyserver.events.OutboundAlertingEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsEndedEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsStartedEvent;
import com.admtel.telephonyserver.events.PlaybackEndedEvent;
import com.admtel.telephonyserver.events.PlaybackStartedEvent;
import com.admtel.telephonyserver.events.asterisk.ASTAsyncAgiEvent;
import com.admtel.telephonyserver.events.asterisk.ASTAgiExecEvent;
import com.admtel.telephonyserver.events.asterisk.ASTChannelState;
import com.admtel.telephonyserver.events.asterisk.ASTDialEvent;
import com.admtel.telephonyserver.events.asterisk.ASTDtmfEvent;
import com.admtel.telephonyserver.events.asterisk.ASTEvent;
import com.admtel.telephonyserver.events.asterisk.ASTHangupEvent;
import com.admtel.telephonyserver.events.asterisk.ASTMeetmeJoinEvent;
import com.admtel.telephonyserver.events.asterisk.ASTMeetmeTalkingEvent;
import com.admtel.telephonyserver.events.asterisk.ASTNewChannelEvent;
import com.admtel.telephonyserver.events.asterisk.ASTNewStateEvent;
import com.admtel.telephonyserver.events.asterisk.ASTResponseEvent;
import com.admtel.telephonyserver.events.asterisk.ASTEvent.EventType;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.core.MessageHandler;

public class ASTChannel extends Channel implements TimerNotifiable {

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
			String actionId = getId() + "___StreamFile";
			ASTChannel.this.session
					.write(String
							.format(
									"Action: AGI\nChannel: %s\nCommand: STREAM FILE %s %s\nActionId: %s\nCommandID: %s",
									getId(), prompts.get(playedPromptCount),
									terminators, actionId, actionId));
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
						ASTChannel.this.onEvent(new PlaybackStartedEvent(
								ASTChannel.this));
					}
				} else {
					// playback ended
					playedPromptCount++;
					if (playedPromptCount == prompts.size()
							|| !interruptingDigit.isEmpty()) {
						if (interruptingDigit.isEmpty()) {
							ASTChannel.this.onEvent(new PlaybackEndedEvent(
									ASTChannel.this, interruptingDigit, ""));
						} else {
							ASTChannel.this.onEvent(new PlaybackEndedEvent(
									ASTChannel.this, interruptingDigit, prompts
											.get(playedPromptCount - 1)));
						}
					} else {
						String actionId = getId() + "___StreamFile";
						ASTChannel.this.session
								.write(String
										.format(
												"Action: AGI\nChannel: %s\nCommand: STREAM FILE %s %s\nActionId: %s\nCommandID: %s",
												getId(),
												prompts.get(playedPromptCount),
												terminators, actionId, actionId));
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

	private class JoinConferenceState extends State{
		String conferenceId;
		boolean moderator;
		boolean muted;
		boolean deaf;
		
		public JoinConferenceState(String conferenceId, boolean moderator, boolean muted, boolean deaf){
			
			this.conferenceId = conferenceId;
			this.moderator = moderator;
			this.muted = muted;
			this.deaf = deaf;
			String parameters = "dT";//Dynamically create the conference with talked detection T
			if (moderator){
				parameters +="a";
			}
			if (muted){
				parameters += "m";
			}
			if (deaf){
				parameters +="t";
			}
	
			String command = String.format("%s|%s|", conferenceId, parameters);
			String actionId = getId() + "___MeetMe";

			ASTChannel.this.session
			.write(String
					.format(
							"Action: AGI\nChannel: %s\nCommand: EXEC MeetMe %s\nActionId: %s\nCommandID: %s",
							getId(), command, actionId, actionId));	
			
		}
		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(ASTEvent astEvent) {
			switch (astEvent.getEventType()){
			case MeetmeJoin:
			{
				ASTMeetmeJoinEvent je = (ASTMeetmeJoinEvent) astEvent;
				ASTChannel.this.conferenceParticipant = new Participant (moderator, muted, deaf);
				ASTChannel.this.conferenceParticipant.setMemeber(je.getUsernum());
				if (Conferences.getInstance().joinConference(conferenceId, ASTChannel.this)){
					ASTChannel.this.conferenceParticipant.setJoinTime(new DateTime());
				}
				ASTChannel.this.onEvent(new ConferenceJoinedEvent(ASTChannel.this, je.getUsernum(), moderator, muted, deaf));
			}
				break;
			case MeetmeLeave:{
				ASTChannel.this.conferenceParticipant = null;
				ASTChannel.this.onEvent(new ConferenceLeftEvent(ASTChannel.this));
			}
				break;
			case MeetmeTalking:{
				ASTMeetmeTalkingEvent mte=(ASTMeetmeTalkingEvent)astEvent;
				ASTChannel.this.conferenceParticipant.setTalking(mte.isOn());
				ASTChannel.this.onEvent(new ConferenceTalkEvent(ASTChannel.this, ASTChannel.this.conferenceParticipant.isTalking()));
			}
				break;
			}
			
			
		}
		
	}
	private class NullState extends State {

		@Override
		public void processEvent(ASTEvent astEvent) {
			switch (astEvent.getEventType()) {
			case NewChannel: {
				ASTNewChannelEvent nce = (ASTNewChannelEvent) astEvent;
				channelData.setCalledNumber(nce.getCalledNum());
				channelData.setCallerIdNumber(nce.getCallerIdNum());
				channelData.setUserName(nce.getUserName());
				channelData.setServiceNumber(nce.getCalledNum());
				switch (nce.getChannelState()) {
				case Ring:
					currentState = new InboundAlertingState();
					break;
				case Ringing:
					currentState = new OutboundAlertingState();
					break;
				case Answer:
					ASTChannel.this.onEvent(new AnsweredEvent(ASTChannel.this));
					currentState = new IdleState();
					break;
				}
			}
				break;
			case NewState: {
				ASTNewStateEvent nse = (ASTNewStateEvent) astEvent;
				switch (nse.getChannelState()) {
				case Ring:
					currentState = new InboundAlertingState();
					break;
				case Ringing:
					currentState = new OutboundAlertingState();
					break;
				case Answer:
					ASTChannel.this.onEvent(new AnsweredEvent(ASTChannel.this));
					currentState = new IdleState();
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

	private class InboundAlertingState extends State {

		public InboundAlertingState() {
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
					ASTChannel.this.getVariable("adm_args");
				}
			}
				break;

			case Response: {
				ASTResponseEvent response = (ASTResponseEvent) astEvent;
				if (response.getRequest().equals("GetVar")) {
					if (response.getValue("adm_args") != null) {
						String admArgs = response.getValue("adm_args");
						channelData.addDelimitedVars(admArgs, "&");
						// Create script
						Script script = ScriptManager.getInstance()
								.createScript(channelData);
						if (script != null) {
							log.debug("Created script " + script);
							listeners.add(script);
						}
						// Send inbound alerting event
						InboundAlertingEvent ie = new InboundAlertingEvent(
								ASTChannel.this);
						ASTChannel.this.onEvent(ie);

					}
				}

			}
				break;

			}
		}
	}

	private class OutboundAlertingState extends State {

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
					ASTChannel.this.getVariable("adm_args");
				}
			}
				break;

			case Response: {
				ASTResponseEvent response = (ASTResponseEvent) astEvent;
				if (response.getRequest().equals("GetVar")) {
					if (response.getValue("adm_args") != null) {
						String admArgs = response.getValue("adm_args");
						channelData.addDelimitedVars(admArgs, "&");
						// Create script
						Script script = ScriptManager.getInstance()
								.createScript(channelData);
						if (script != null) {
							listeners.add(script);
						}
						// Send outbound alerting event
						OutboundAlertingEvent oa = new OutboundAlertingEvent(
								ASTChannel.this);
						ASTChannel.this.onEvent(oa);
						//In the case of asterisk, we know that we're here only because the channel was
						// answered
						AnsweredEvent ae = new AnsweredEvent(ASTChannel.this);
						ASTChannel.this.currentState = new IdleState();
						ASTChannel.this.onEvent(ae);						
					}
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

		public PlayingAndGettingDigitsState(int max, String prompt,
				long timeout, String terminators, boolean interruptPlay) {
			this.prompts.add(prompt);
			this.max = max;
			this.timeout = timeout;
			this.terminators = terminators;
			this.interruptPlay = interruptPlay;
			playedPromptCount = 0;
			execute(); // TODO check return result
		}

		public PlayingAndGettingDigitsState(int max, String[] prompts,
				long timeout, String terminators, boolean interruptPlay) {
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

								pe = new PlayAndGetDigitsEndedEvent(
										ASTChannel.this, this.digits);
								pe.setTerminatingDigit(termChar);
								if (this.playedPromptCount < prompts.size()) {
									pe.setInterruptedFile(prompts
											.get(this.playedPromptCount));
								}

								substate = ENDED;
								ASTChannel.this.currentState = new IdleState();
								ASTChannel.this.onEvent(pe);

							} else {
								String termChar = "";
								if (this.terminators.indexOf(this.digits) != -1) {
									termChar = this.digits;
									this.digits = "";
									PlayAndGetDigitsEndedEvent pe = null;

									pe = new PlayAndGetDigitsEndedEvent(
											ASTChannel.this, this.digits);
									pe.setTerminatingDigit(termChar);
									if (this.playedPromptCount < prompts.size()) {
										pe.setInterruptedFile(prompts
												.get(this.playedPromptCount));
									}
									substate = ENDED;
									ASTChannel.this.currentState = new IdleState();
									ASTChannel.this.onEvent(pe);

								} else {
									substate = GETTING_DIGITS;
									this.maxDtmfTimer = Timers.getInstance()
											.startTimer(ASTChannel.this,
													timeout, true, this);
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
						if (termChar.length() > 0
								|| this.digits.length() == this.max) {
							PlayAndGetDigitsEndedEvent pe = new PlayAndGetDigitsEndedEvent(
									ASTChannel.this, this.digits);
							pe.setTerminatingDigit(termChar);
							if (this.playedPromptCount < prompts.size()) {
								pe.setInterruptedFile(prompts
										.get(this.playedPromptCount));
							}

							substate = ENDED;
							ASTChannel.this.currentState = new IdleState();
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
						ASTChannel.this
								.onEvent(new PlayAndGetDigitsStartedEvent(
										ASTChannel.this));
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
											.format(
													"Action: AGI\nChannel: %s\nCommand: STREAM FILE %s %s\nActionId: %s\nCommandID: %s",
													getId(),
													prompts
															.get(playedPromptCount),
													interruptDigits, actionId,
													actionId));

						} else {// that was the last prompt to play
							substate = GETTING_DIGITS;
							this.maxDtmfTimer = Timers.getInstance()
									.startTimer(ASTChannel.this, timeout, true,
											this);
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
				ASTChannel.this.session
						.write(String
								.format(
										"Action: AGI\nChannel: %s\nCommand: STREAM FILE %s %s\nActionId: %s\nCommandID: %s",
										getId(),
										prompts.get(playedPromptCount),
										interruptDigits, actionId, actionId));
				substate = PLAYING;

			} else {
				// Send started event
				ASTChannel.this.onEvent(new PlayAndGetDigitsStartedEvent(
						ASTChannel.this));
				// Start the timer
				substate = GETTING_DIGITS;
				this.maxDtmfTimer = Timers.getInstance().startTimer(
						ASTChannel.this, timeout, true, this);
			}

			return Result.Ok;
		}

		@Override
		public boolean onTimer(Object data) {
			if (data == this) {// it is our own timer
				if (substate == GETTING_DIGITS) {
					PlayAndGetDigitsEndedEvent pe = new PlayAndGetDigitsEndedEvent(
							ASTChannel.this, this.digits);
					substate = ENDED;
					ASTChannel.this.currentState = new IdleState();
					ASTChannel.this.onEvent(pe);
				}
			}
			return true; // stop the timer, eventhought it is a one shot timer,
			// it doesn't hurt to make sure it is removed
		}
	}

	private class DialingState extends State {

		public DialingState(String address, long timeout) {

			String dialStr = String.format("%s||%d", address, timeout);

			String actionId = getId() + "___Dial";
			ASTChannel.this.session
					.write(String
							.format(
									"Action: AGI\nChannel: %s\nCommand: EXEC DIAL %s\nActionId: %s\nCommandID: %s",
									getId(), dialStr, actionId, actionId));
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
			case Dial: {
				ASTDialEvent dialEvent = (ASTDialEvent) astEvent;
				if (dialEvent.isBegin()) {
					log.debug(String.format(
							"DialedEvent form channel %s ---> %s", dialEvent
									.getChannelId(), dialEvent
									.getDestinationChannel()));
					Channel dialedChannel = ASTChannel.this._switch
							.getChannel(dialEvent.getDestinationChannel());
					onEvent(new DialStartedEvent(ASTChannel.this, dialedChannel));
				}
			}
				break;
			}

		}

	}

	// END STATES LOGIC
	// ////////////////////////////////////////////////////////////////////////////////////////

	State currentState = new NullState();
	IoSession session;
	MessageHandler messageHandler = new QueuedMessageHandler() {

		@Override
		public void onMessage(Object message) {

			if (message == null || !(message instanceof ASTEvent)) {
				return;
			}
			ASTEvent astEvent = (ASTEvent) message;
			if (astEvent == null)
				return;
			log
					.debug(String
							.format(
									"START processing event (%s) state (%s), internalState(%s)",
									astEvent, state, currentState.getClass()
											.getSimpleName()));

			switch (astEvent.getEventType()) {
			case Hangup: {
				ASTHangupEvent asthe = (ASTHangupEvent) astEvent;
				HangupEvent he = new HangupEvent(ASTChannel.this);
				he.setHangupCauseStr(asthe.getCauseTxt());
				try{
					he.setHangCause(Integer.parseInt(asthe.getCause()));
				}
				catch (Exception e){
					he.setHangCause(16);
				}
				ASTChannel.this.onEvent(he);
			}
				break;
			case NewState: {
				ASTNewStateEvent nse = (ASTNewStateEvent) astEvent;
				if (nse.getChannelState() == ASTChannelState.Answer) {
					ASTChannel.this.onEvent(new AnsweredEvent(ASTChannel.this));
				}
			}
				break;
			}
			if (currentState != null) {
				currentState.processEvent(astEvent);
			}

			if (astEvent.getEventType() == EventType.Hangup) {
				listeners.clear();
				ASTChannel.this._switch.removeChannel(ASTChannel.this);
			}
			log.debug(String.format(
					"END processing event (%s) state (%s), internalState(%s)",
					astEvent, state, currentState.getClass().getSimpleName()));

		}

	};

	public ASTChannel(Switch _switch, String id, IoSession session) {
		super(_switch, id);
		this.session = session;
	}

	@Override
	public Result internalAnswer() {
		ASTChannel.this.session
				.write(String
						.format(
								"Action: AGI\nChannel: %s\nCommand: Answer\nActionId: %s\nCommandId: %s",
								getId(), getId() + "___Answer", getId()
										+ "___Answer"));
		return Result.Ok;
	}

	@Override
	public Result internalHangup(String cause) {
		ASTChannel.this.session.write(String.format(
				"Action: Hangup\nChannel: %s", getId()));
		return Result.Ok;
	}

	@Override
	public Result internalDial(String address, long timeout) {
		String translatedAddress = _switch.addressTranslator.translate(address);
		if (translatedAddress != null && translatedAddress.length() > 0) {
			currentState = new DialingState(translatedAddress, timeout);
		} else {
			log.warn(String.format("%s, invalid dial string %s", this.getId(),
					address));
			return Result.InvalidParameters;
		}
		return currentState.getResult();

	}

	@Override
	public Result internalPlayAndGetDigits(int max, String prompt,
			long timeout, String terminators, boolean interruptPlay) {
		currentState = new PlayingAndGettingDigitsState(max, prompt, timeout,
				terminators, interruptPlay);
		return Result.Ok;
	}

	@Override
	public Result internalPlayAndGetDigits(int max, String[] prompt,
			long timeout, String terminators, boolean interruptPlay) {
		currentState = new PlayingAndGettingDigitsState(max, prompt, timeout,
				terminators, interruptPlay);
		return Result.Ok;
	}

	@Override
	public Result internalPlayback(String[] prompt, String terminators) {
		currentState = new PlayingState(prompt, terminators);

		return Result.Ok;
	}

	@Override
	public Result internalPlayback(String prompt, String terminators) {
		currentState = new PlayingState(prompt, terminators);
		return Result.Ok;
	}

	public void processNativeEvent(ASTEvent astEvent) {
		log.debug(astEvent + ":State(" + currentState + ")");
		messageHandler.putMessage(astEvent);
	}

	private void getVariable(String varname) {
		session.write(String.format(
				"ACTION: GetVar\nChannel: %s\nVariable: %s\nActionID: %s", this
						.getId(), varname, getId() + "___GetVar"));
	}

	@Override
	synchronized public boolean onTimer(Object data) {
		log.debug("Got timer .... " + data);
		if (currentState != null) {
			return currentState.onTimer(data);
		}
		return false;
	}

	@Override
	public
	Result internalJoinConference(String conferenceId, boolean moderator, boolean startMuted, boolean startDeaf) {
		currentState = new JoinConferenceState(conferenceId, moderator, startMuted, startDeaf);//TODO check state
		return Result.Ok;
	}

	public String getSwitchId() {
		return null;
	}
}