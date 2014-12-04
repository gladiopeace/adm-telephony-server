package com.admtel.telephonyserver.scripts;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.events.DisconnectCode;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.AlertingEvent;
import com.admtel.telephonyserver.events.OfferedEvent;
import com.admtel.telephonyserver.events.PlayAndGetDigitsEndedEvent;
import com.admtel.telephonyserver.events.PlaybackEndedEvent;
import com.admtel.telephonyserver.events.PlaybackStartedEvent;


public class SimpleTestScript extends Script {

	enum State {
		Null, Answering, Answered, PlayingWelcome, GettingAccountNumber,
	};

	State state = State.Null;

	Channel a = null;

	static Logger log = Logger.getLogger(SimpleTestScript.class);
	String displayStr;

	@Override
	protected void processEvent (Event event) {

		log.debug(String.format("Script(%s) state (%s) got event (%s)", this,
				state, event));
		
		switch (state) {
		case Null:
			processNullState(event);
			break;
		case Answering:
			processAnsweringState(event);
			break;

		case PlayingWelcome:
			processPlayingWelcomeState(event);
			break;
		case GettingAccountNumber:
			processPGettingAccountNumberState(event);
			break;
		}
	}

	private void processPGettingAccountNumberState(Event event) {
		switch (event.getEventType()){
			case PlayAndGetDigitsEnded:{
				PlayAndGetDigitsEndedEvent e = (PlayAndGetDigitsEndedEvent) event;
				displayStr = "Got digits " + e.getDigits();
				log.debug("Got digits " + e.getDigits());
				e.getChannel().hangup(DisconnectCode.Normal);
			}
				break;
		}
		
	}

	private void processNullState(Event event) {
		switch (event.getEventType()){
		case Offered:{
			OfferedEvent ie = (OfferedEvent) event;
			a = ie.getChannel();
			if (a == null){
				log.fatal(this +", couldn't find the channel");
				return;
			}
			a.answer();
			state = State.Answering;
		}
		break;
		}
		
	}

	private void processAnsweringState(Event event) {
		
		switch (event.getEventType()){
		case Connected:
		{
			
			String[] prompts={"ivr/ivr-welcome_to_freeswitch"};
			log.debug("PlayAndGetDigits ....");
			a
			.playAndGetDigits(
					4,
					 prompts
					,
					5000, "#");
			displayStr = "Getting digits for welcome";
	state = State.PlayingWelcome;
		}
	break;
		}

	}

	private void processPlayingWelcomeState(Event e) {
		switch (e.getEventType()) {
		case PlaybackStarted: {
			PlaybackStartedEvent event = (PlaybackStartedEvent) e;
			log.debug("Playback started");
		}
			break;
		case PlaybackEnded: {
			PlaybackEndedEvent event = (PlaybackEndedEvent) e;
			log.debug("Playback ended interrupted By " + event.getInterruptingDigit());
		}
		break;
		case PlayAndGetDigitsEnded:{
			PlayAndGetDigitsEndedEvent event = (PlayAndGetDigitsEndedEvent) e;
			log.debug("Got digits " + event.getDigits());
			String[] prompts={"ivr/ivr-sample_submenu",
			"ivr/ivr-account_number"};
	a
	.playAndGetDigits(
			4,
			 prompts
			,
			5000, "#");

			this.state = State.GettingAccountNumber;
		}
			break;
		}
	}

	@Override
	public void onTimer() {
		log.debug("Script " + this + ", onTimeout "
				+ System.currentTimeMillis());

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		log.debug("Script stopped, all channels will be gone after returning, please don't attempt to do anything with the channels");
	}

	@Override
	public String getDisplayStr() {
		return displayStr;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}


}
