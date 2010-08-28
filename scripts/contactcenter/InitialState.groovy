package contactcenter

import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.*
import com.admtel.telephonyserver.events.*
import com.admtel.telephonyserver.radius.*;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.prompts.*;

import org.apache.log4j.Logger;

public class InitialState{
	
	Script script
	
	InitialState (script){
		this.script = script
	}
	def onInboundAlerting (InboundAlertingEvent evt){
		evt.getChannel().answer()
		this
	}
	def onAnswered(AnsweredEvent e){
		[script, e.getChannel(), '6500'] as QueueState
	}
}

public class QueueState{
	
	static final Logger log = Logger.getLogger(QueueState.class);
	
	Script script
	QueueState(script, channel, queueName){
		this.script = script
		channel.queue(queueName)
	}
	
	def onQueueJoined(QueueJoinedEvent e){
		log.trace("Channel ${e.getChannel()} joined Queue ${e.getQueueName()}")
		this	
	}
	def onQueueLeft(QueueLeftEvent e){
		log.trace("Channel ${e.getChannel()} Left Queue ${e.getQueueName()}")
		this
	}
}