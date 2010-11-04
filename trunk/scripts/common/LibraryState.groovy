package common

import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.events.DialFailedEvent;
import com.admtel.telephonyserver.events.DialStartedEvent;
import com.admtel.telephonyserver.events.HangupEvent;

public class LibraryState{
	
}

public class DialState{
	
	Channel b
	Channel a
	def routes
	def onFailedState
	
	DialState(channel, route, onFailedState){		
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