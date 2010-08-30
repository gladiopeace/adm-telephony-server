package softswitch

import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.*
import com.admtel.telephonyserver.events.*
import com.admtel.telephonyserver.radius.*;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.prompts.*;
import java.util.*;

import org.apache.log4j.Logger;

public class InitialState{
	
	Script script
	
	InitialState (script){
		this.script = script
	}
	def onInboundAlerting (InboundAlertingEvent e){
		e.getChannel().answer()
	}
	def onAnswered(AnsweredEvent e){
		AuthorizeResult result = Radius.authorize(e.getChannel(), e.getChannel().getAccountCode(),
				"", e.getChannel().getLoginIP(), "Login-User", e.getCallerIdNumber(), e.getCalledIdNumber(), true, true)
		if (result?.getAuthorized()){
			if (result.getRoutes().size()>0){
				[script, e.getChannel(), result.getRoutes()] as DialState
			}
		}
		
		[script, e.getChannel(), DisconnectCode.Normal] as HangupState
	}
}

public class HangupState{
	Script script
	HangupState(script, channel, reason){
		channel.hangup(reason)
	}
}


public class DialState{
	
	
	Script script
	def routes
	int currentRoute = 0
	
	DialState(script, channel, route){
		this.script = script
		this.routes = route
		channel.dial(routes[currentRoute], 10000)
	}
	DialState(Script, channel){
		this.script = script
		channel.dial(routes[currentRoute], 10000)
	}
	def onDialStarted(DialStartedEvent e){
		this
	}
	def onDialFailed(DialFailedEvent e){
		currentRoute ++
		if (routes.size()>currentRoute){
			[script, e.getChannel()] as DialState
		}
		[script, e.getChannel()] as HangupState
	}
}