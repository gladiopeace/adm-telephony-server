package callingcard

import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.*;
import com.admtel.telephonyserver.core.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
public class InitialState{
	
	CallingCard script
	
	InitialState (script){
		this.script = script
	}
	def onInboundAlerting (InboundAlertingEvent e){
		
//		AuthorizeResult result = Radius.authorize(e.getChannel(), e.getChannel().getAccountCode(),
//				"", e.getChannel().getLoginIP(), "Login-User", e.getCallerIdNumber(), e.getCalledIdNumber(), false, false)
//		
//		if (result?.getAuthorized()){
//			if (result.getRoutes().size()>0){
//				[script, e.getChannel(), result.getRoutes()] as GetDestinationState
//			}
//		}
//		else{
//			[script, e.getChannel(), DisconnectCode.Normal] as GetAccountState
//		}
		e.getChannel().answer();
		this
	}
	def onAnswered(AnsweredEvent e){
		println "***************** got answered event"
		[e.getChannel()] as GetAccountState
	}
}

public class GetAccountState{
	int counter = 0
	GetAccountState(channel){
		Channel c = channel
		println "Playing callingcard/accountnum"
		c.playAndGetDigits(10, "callingcard/accountnum", 10000, "#")
	}
	
	def onPlayAndGetDigitsEnded (PlayAndGetDigitsEndedEvent e){
		counter ++
		if (counter < 3){
			e.getChannel().playAndGetDigits(10, "callingcard/accountnum", 10000, "#")
			this
		}
		else{
			[e.getChannel()] as HangupState
		}
	}
	def onPlayAndGetDigitsFailed(PlayAndGetDigitsFailedEvent e){
		this
	}
	
}

public class HangupState{
	HangupState(channel){
		channel.hangup()
	}
}
///////////////////////////////////////////////////////////////////////////////////////////////////