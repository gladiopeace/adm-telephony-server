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

///////////////////////////////////////////////////////////////////////////////////////////////////
public class InitialState{
	
	SoftSwitch script
	
	InitialState (script){
		this.script = script
	}
	def onInboundAlerting (InboundAlertingEvent e){
		
		AuthorizeResult result = Radius.authorize(e.getChannel(), e.getChannel().getAccountCode(),
				"", e.getChannel().getLoginIP(), "Login-User", e.getCallerIdNumber(), e.getCalledIdNumber(), true, true)
		
		def channel = e.getChannel()
		def routes = result.getRoutes();
		
		if (result?.getAuthorized()){
			if (result.getRoutes().size()>0){
				[script, e.getChannel(), result.getRoutes()] as DialState
			}
		}
		else{
			[script, e.getChannel(), DisconnectCode.Normal] as HangupState
		}
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class HangupState{
	Script script
	HangupState(script, channel, reason){
		channel.hangup(reason)
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class DialState{
	
	
	SoftSwitch script
	Channel b
	Channel a
	def routes
	DialState(script, channel, route){
		this.script = script
		this.a=channel
		this.b=null
		this.routes = route
		script.log.trace("********** $channel, dialing " + routes[0])
		a.dial(routes[0], 10000)
	}
	def onDialStarted(DialStartedEvent e){
		routes.remove 0
		script.log.trace("*************** " + e.getChannel()+" (inbound) ---- "+e.getDialedChannel()+"(outbound)")
		b = e.getDialedChannel()
		this
	}
	def onHangup(HangupEvent e){
		script.log.trace("*********************Hangup received for channel "+e.getChannel())
		if (e.getChannel()==b && routes.size()>0){
			return [script, e.getChannel(), routes] as DialState
		}
		else{
			return this
		}
	}
	def onDialFailed(DialFailedEvent e){
		currentRoute ++
		if (routes.size()>currentRoute){
			[script, e.getChannel()] as DialState
		}
		[script, e.getChannel()] as HangupState
	}
}
///////////////////////////////////////////////////////////////////////////////////////////////////