package com.admtel.telephonyserver.freeswitch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.admtel.telephonyserver.config.SwitchType;
import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.MessageHandler;
import com.admtel.telephonyserver.core.QueuedMessageHandler;
import com.admtel.telephonyserver.core.Result;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.core.ScriptManager;
import com.admtel.telephonyserver.core.Switch;
import com.admtel.telephonyserver.events.AnsweredEvent;
import com.admtel.telephonyserver.events.DialFailedEvent;
import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;
import com.admtel.telephonyserver.events.OutboundAlertingEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsEndedEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsFailedEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsStartedEvent;
import com.admtel.telephonyserver.events.PlaybackEndedEvent;
import com.admtel.telephonyserver.events.PlaybackFailedEvent;
import com.admtel.telephonyserver.events.PlaybackStartedEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelCreateEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelDataEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelDestroyEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelOriginateEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelOutgoingEvent;
import com.admtel.telephonyserver.freeswitch.events.FSCommandReplyEvent;
import com.admtel.telephonyserver.freeswitch.events.FSDtmfEvent;
import com.admtel.telephonyserver.freeswitch.events.FSEvent;
import com.admtel.telephonyserver.freeswitch.events.FSEvent.EventType;
import com.admtel.telephonyserver.utils.AdmUtils;
import com.admtel.telephonyserver.utils.CodecsUtils;
import com.admtel.telephonyserver.utils.PromptsUtils;

public class FSChannel extends Channel {

	private IoSession session;
	private State currentState = new NullState();
	
	static Logger log = Logger.getLogger(FSChannel.class);
	
	private abstract class State {
		public abstract void processEvent(FSEvent fsEvent);

		public abstract boolean onTimer(Object data);

		Result result;

		public void setResult(Result result) {
			this.result = result;
		}

		public Result getResult() {
			return this.result;
		}
	}
	private class IdleState extends State{

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(FSEvent fsEvent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class OutboundAlertingState extends State{

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(FSEvent fsEvent) {
			switch (fsEvent.getEventType()){
			case ChannelData:{
				FSChannelDataEvent cde = (FSChannelDataEvent) fsEvent;
				String admArgs = cde.getValue("variable_adm_args");
				getChannelData().addDelimitedVars(admArgs, "&");
				getChannelData().setCalledNumber(cde.getCalledIdNum());
				getChannelData().setCallerIdNumber(cde.getCallerIdNum());
				getChannelData().setCallerIdName(cde.getCallerIdNum());
				// Create script
				Script script = ScriptManager.getInstance()
						.createScript(getChannelData());
				if (script != null) {
					getListeners().add(script);
				}
				// Send outbound alerting event
				OutboundAlertingEvent ie = new OutboundAlertingEvent(
						FSChannel.this, FSChannel.this.getCallingStationId(), FSChannel.this.getCalledStationId());
				FSChannel.this.onEvent(ie);
			}
				break;
			}
			
		}
		
	}
	private class InboundAlertingState extends State{

		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(FSEvent fsEvent) {
			switch (fsEvent.getEventType()){
			case ChannelData:{
				FSChannelDataEvent cde = (FSChannelDataEvent) fsEvent;
				String admArgs = cde.getValue("variable_adm_args");
				getChannelData().addDelimitedVars(admArgs, "&");
				getChannelData().setCalledNumber(cde.getCalledIdNum());
				getChannelData().setCallerIdNumber(cde.getCallerIdNum());
				getChannelData().setCallerIdName(cde.getCallerIdNum());

				// Create script
				Script script = ScriptManager.getInstance()
						.createScript(getChannelData());
				if (script != null) {
					getListeners().add(script);
				}
				// Send inbound alerting event
				InboundAlertingEvent ie = new InboundAlertingEvent(
						FSChannel.this);
				FSChannel.this.onEvent(ie);
			}
				break;
			}
			
		}
		
	}
	private class NullState extends State {

		@Override
		public void processEvent(FSEvent event) {

			Event result = null;

			switch (event.getEventType()) {
			case ChannelCreate:
			{
				FSChannelCreateEvent cce = (FSChannelCreateEvent) event;

				if (cce.isOutbound()){
					switch (cce.getChannelState()){
					case Ringing:
						 currentState = new OutboundAlertingState();
						break;
					}
				}
				else{
					switch (cce.getChannelState()){
					case Ringing:
						currentState = new InboundAlertingState();
						break;
					}
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
	private class PlayingAndGettingDigits extends State{
		private int max;
		private long timeout;
		private String terminators;
		private boolean interruptPlay;
		private String prompt;
		private String termDigit;

		public PlayingAndGettingDigits(int max, String prompt,
				long timeout, String terminators, boolean interruptPlay) {
			
			this.max = max;
			this.timeout = timeout;
			this.terminators = terminators;
			this.interruptPlay = interruptPlay;
			this.prompt = prompt;
			this.termDigit = "";
			execute();

		} 
		public PlayingAndGettingDigits(int max, String[] prompts,
				long timeout, String terminators, boolean interruptPlay) {
			this.prompt = PromptsUtils.expandPrompts(prompts, "&", SwitchType.Freeswitch);
			this.max = max;
			this.timeout = timeout;
			this.terminators = terminators;
			this.interruptPlay = interruptPlay;
			this.termDigit ="";
			execute();
		}
		@Override
		public void processEvent(FSEvent event) {

			switch (event.getEventType()){
			case CommandReply: {
				FSCommandReplyEvent cre = (FSCommandReplyEvent) event;
				if (!cre.isSuccess()){
					 FSChannel.this.onEvent(new PlayAndGetDigitsFailedEvent(FSChannel.this, cre.getResultDescription()));
				}
			}
				break;
			case ChannelExecute: { // TODO look for failure events 	
				String application = event.getValue("Application");
				if (application.equals("read")){
					FSChannel.this.onEvent(new PlayAndGetDigitsStartedEvent(FSChannel.this));
				}
			}
				break;
			case ChannelExecuteComplete: { // TODO look for failure events
				String application = event
						.getValue("variable_current_application");
				if (application.equals("read")) {
					String readResult = event.getValue("variable_read_result");
					if (readResult != null && readResult.equals("failure")){
						FSChannel.this.onEvent(new PlayAndGetDigitsFailedEvent(FSChannel.this, readResult));
					}
					else{
						String digits = event.getValue("variable_digits");
						PlayAndGetDigitsEndedEvent pee =  new PlayAndGetDigitsEndedEvent(FSChannel.this, digits);
						pee.setTerminatingDigit(this.termDigit);
						FSChannel.this.onEvent(pee);
						
					}
				}
			}
				break;
			case DTMF:
			{
				FSDtmfEvent dtmfEvent = (FSDtmfEvent) event;
				if (terminators.indexOf(dtmfEvent.getDtmf())!=-1){
					this.termDigit = dtmfEvent.getDtmf();
				}
			}
			break;	
			}
		}

		
		public void execute() {

			session.write(buildMessage(getId(), "execute", "set",
					"playback_delimiter=&"));
			if (!this.interruptPlay){
				session.write(buildMessage(getId(), "execute","set","sleep_eat_digits=false"));
			}
			else{
				session.write(buildMessage(getId(), "execute","set","sleep_eat_digits=true"));
			}
			session.write(buildMessage(getId(), "execute", "read", String
					.format("%d %d %s digits %d %s", 0, max, prompt, timeout,
							terminators)));
						
		}
		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}		
	}
	private class PlayingbackState extends State{
		
		public PlayingbackState(String prompt, String terminators){
			
			session.write(buildMessage(getId(), "execute", "set",
			"playback_delimiter=&"));
			session.write(buildMessage(getId(), "execute", "set",
					"playback_terminators=" + terminators));
			session.write(buildMessage(getId(), "execute", "playback", prompt));			
		}

		@Override
		public void processEvent(FSEvent event) {
			Event result = null;
			switch (event.getEventType()){
			case CommandReply: {
				FSCommandReplyEvent cre = (FSCommandReplyEvent) event;
				if (!cre.isSuccess()){
					result = new PlaybackFailedEvent(FSChannel.this, cre.getResultDescription());
				}
			}
				break;
			case ChannelExecute: { // TODO look for failure events 
				String application = event.getValue("Application");
				if (application.equals("playback")){
					result = new PlaybackStartedEvent(FSChannel.this);
				}
			}
				break;
			case ChannelExecuteComplete: { // TODO look for failure events
				String application = event
						.getValue("Application");
				if (application.equals("playback")) {		
					String returnCode = CodecsUtils.urlDecode(event.getValue("variable_current_application_response", ""));
					log.debug("Return code is "+returnCode);
					if (returnCode.equals("FILE PLAYED")){
						result = new PlaybackEndedEvent (FSChannel.this, event.getValue("variable_playback_terminator_used",""),"");
					}
					else{
						result = new PlaybackFailedEvent (FSChannel.this, returnCode); //TODO return unified error codes
					}
					currentState = new IdleState();
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
	private class DialingState extends State{
		public DialingState(String address, long timeout) {
			session.write(buildMessage(getId(),"execute", "bridge", address));
		}
		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(FSEvent fsEvent) {
			switch (fsEvent.getEventType()){
			case CommandReply: {
				FSCommandReplyEvent cre = (FSCommandReplyEvent) fsEvent;
				if (!cre.isSuccess()){
					FSChannel.this.onEvent (new DialFailedEvent (FSChannel.this));
				}
			}
				break;
			case ChannelOriginate:
			{
				FSChannelOriginateEvent coe = (FSChannelOriginateEvent) fsEvent;
				FSChannel otherChannel = (FSChannel) FSChannel.this._switch.getChannel(coe.getChannelId());
				FSChannel.this.onEvent(new DialStartedEvent(FSChannel.this, otherChannel));
			}
				break;

			}
			
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
			String flags="";
			String command;
			if (moderator){
				flags = AdmUtils.addWithDelimiter(flags, "moderator", "|");
			}
			if (muted){
				flags = AdmUtils.addWithDelimiter(flags, "mute", "|");
			}
			if (deaf){
				flags = AdmUtils.addWithDelimiter(flags, "deaf", "|");
			}
			if (!flags.isEmpty()){
				command = String.format("conferenceId+flags{%s}", flags);
			}
			else{
				command = conferenceId;
			}
			session.write(buildMessage(getId(),"execute", "conference", command));
		}
		@Override
		public boolean onTimer(Object data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void processEvent(FSEvent fsEvent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private MessageHandler messageHandler = new QueuedMessageHandler() {

		@Override
		public void onMessage(Object message) {
			if (message == null || !(message instanceof FSEvent)) {
				return;
			}
			FSEvent fsEvent = (FSEvent) message;
			if (fsEvent == null)
				return;
			log
					.debug(String
							.format(
									"START processing event (%s) state (%s), internalState(%s)",
									fsEvent, state, currentState.getClass()
											.getSimpleName()));

			switch (fsEvent.getEventType()) {
			case ChannelDestroy: {
				FSChannelDestroyEvent cde = (FSChannelDestroyEvent) fsEvent;
			}
				break;
			case ChannelHangup:{
				FSChannel.this.onEvent(new HangupEvent(FSChannel.this));
			}
			break;
			case ChannelAnswered:{
				currentState = new IdleState();
				FSChannel.this.onEvent(new AnsweredEvent(FSChannel.this));
			}
			break;
			}
			if (currentState != null) {
				currentState.processEvent(fsEvent);
			}

			if (fsEvent.getEventType() == EventType.ChannelDestroy) {
				getListeners().clear();
				FSChannel.this._switch.removeChannel(FSChannel.this);
			}
			log.debug(String.format(
					"END processing event (%s) state (%s), internalState(%s)",
					fsEvent, state, currentState.getClass().getSimpleName()));

		}
	};
	
	public FSChannel(Switch _switch, String id, IoSession session) {
		super(_switch, id);
		setIoSession(session);
	}

	public void setIoSession(IoSession session){
		this.session = session;
	}
	@Override
	public Result internalAnswer() {
		session.write(buildMessage(FSChannel.this.getId(), "execute", "answer", ""));
		return Result.Ok;
	}

	@Override
	public Result internalDial(String address, long timeout) {
		String translatedAddress = _switch.getAddressTranslator().translate(address);
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
	public Result internalHangup(Integer cause) {
		session.write(String.format(
				"SendMsg %s\ncall-command: hangup\nhangup-cause: %d", getId(),
				cause));		
		return Result.Ok;
	}

	@Override
	public Result internalPlayAndGetDigits(int max, String prompt,
			long timeout, String terminators, boolean interruptPlay) {
		currentState = new PlayingAndGettingDigits(max, prompt, timeout, terminators, interruptPlay);
		return currentState.getResult();
	}

	@Override
	public Result internalPlayAndGetDigits(int max, String[] prompt,
			long timeout, String terminators, boolean interruptPlay) {
		currentState = new PlayingAndGettingDigits(max, prompt, timeout, terminators, interruptPlay);
		return currentState.getResult();
	}

	@Override
	public Result internalPlayback(String[] prompt, String terminators) {
		String p = PromptsUtils.expandPrompts(prompt, "&", SwitchType.Freeswitch);
		return internalPlayback(p, terminators);
	}

	@Override
	public Result internalPlayback(String prompt, String terminators) {
		currentState = new PlayingbackState(prompt, terminators);
		return currentState.getResult();
	}

	@Override
	public Result internalJoinConference(String conferenceId,
			boolean moderator, boolean startMuted, boolean startDeaf) {
		currentState = new JoinConferenceState(conferenceId, moderator, startMuted, startDeaf);
		return currentState.getResult();
	}

	public void processNativeEvent(FSChannelEvent channelEvent) {
		messageHandler.putMessage(channelEvent);
		
	}
	private String buildMessage(String uuid, String command, String app,
			String arg) {
		return String
				.format(
						"SendMsg %s\ncall-command: %s\nexecute-app-name: %s\nexecute-app-arg: %s\n",
						uuid, command, app, arg);
	}

}
