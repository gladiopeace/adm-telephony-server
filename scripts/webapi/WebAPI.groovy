import org.mortbay.jetty.HttpStatus;

import com.admtel.telephonyserver.core.*;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import com.admtel.telephonyserver.requests.*;
import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;
import groovy.xml.MarkupBuilder;
import com.admtel.telephonyserver.httpserver.AdmServlet;
import com.admtel.telephonyserver.interfaces.TokenSecurityProvider;
import com.admtel.telephonyserver.acd.*;
import com.admtel.telephonyserver.events.DisconnectCode;
import java.net.URLDecoder;
import net.sf.json.groovy.JsonGroovyBuilder;

class WebAPI extends AdmServlet {
	
	static Logger log = Logger.getLogger(WebAPI.class)
	
	
	TokenSecurityProvider securityProvider;
	
	def index(request){
		println "*********** index ${securityProvider}"
		"Welcome"
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def hangup(request){		
		HangupRequest hangupRequest = new HangupRequest(request.getParameter('channel'), DisconnectCode.Normal) 
		Switches.getInstance().processRequest(hangupRequest)
		"Channel hangup request " + request.getParameters('channel')
	}	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def participant_disconnect(request){
		Conference c = ConferenceManager.getInstance().getConferenceById(request.getParameter('conference'))
		if (c == null){
			return "conference not found"
		}
		Participant p = c.getParticipant(request.getParameter('participant'))
		if (p == null){
			return "Participant not found"
		}
		HangupRequest hangupRequest = new HangupRequest(p.getChannel().getId(), DisconnectCode.Normal)
		Switches.getInstance().processRequest(hangupRequest)
		"Participant " + request.getParameter('participant') + " on channel " + p.getChannel().getUniqueId()+", disconnected"
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def participant_mute(request){
		Conference c = ConferenceManager.getInstance().getConferenceById(request.getParameter('conference'))
		if (c == null){
			return "conference not found"
		}
		Participant p = c.getParticipant(request.getParameter('participant'))
		if (p==null) return "Participant not found"
		String mutedStr = request.getParameter('mute')
		
		boolean muted = false
		if (mutedStr != null){
			muted = Boolean.parseBoolean(mutedStr)
		}
		
		ParticipantMuteRequest pmr = new ParticipantMuteRequest(p.getChannel().getUniqueId(), muted )
		Switches.getInstance().processRequest(pmr)
		"Participant ${p.getChannel().getUniqueId()} mute = ${muted}"
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def conference_details(request){
		Conference c = ConferenceManager.getInstance().getConferenceById(request.getParameter('conference'))
		def writer = new StringWriter()
		if (c != null){
			List<Participant> p = c.getParticipants()						
			def xml = new MarkupBuilder(writer)
			
			xml.'document'(type: "conference/xml") {
				conference(id:c.id){
					participants(){
						p.each{
							Channel channel = it.getChannel()
							participant(
									id:uniqueId, 
									time:it.joinTime, 
									caller:channel?.getCallingStationId(), 
									memberId:it.memberId, 
									talking:it.isTalking(), 
									deaf:it.isDeaf(), 
									moderator:it.isModerator(), 
									muted:it.isMuted())
						}
					}
				}
			}
		}
		writer.toString()
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def dial(request){
		String destination = URLDecoder.decode(request.getParameter('destination'))
		String channel = request.getParameter('channel')
		int timeout = Integer.valueOf(request.getParameter('timeout'))
		DialRequest dialRequest = new DialRequest( channel, destination, timeout)
		Switches.getInstance().processRequest(dialRequest)
		"${channel} -> Dialed -> ${destination}"
	}
	def join_conference(request){
		String channel = request.getParameter('channel')
		Channel c = Switches.getInstance().getChannelById(channel)
		if (c){		
			String conferenceNumber = request.getParameter('conferenceNumber')
			if (!conferenceNumber)
				conferenceNumber = c.getUserData("conferenceNumber")
			JoinConferenceRequest jcr = new JoinConferenceRequest(channel, conferenceNumber, false, false, false)
			Switches.getInstance().processRequest(jcr)
		}// TODO result code
	}
	
	def get_channel_data(request){
		String channelId = request.getParameter('channel')
		String keyId = request.getParameter('key')
		Channel channel = Switches.getInstance().getChannelById(channelId) 
		def result = new JsonGroovyBuilder().json{
			key = keyId
			value = channel?.getUserData(keyId)
		}.toString()
	}
	def set_channel_data(request){
		String channelId = request.getParameter('channel')
		String key = request.getParameter('key')
		String value = request.getParameter('value')
		Channel channel = Switches.getInstance().getChannelById(channelId)
		if (channel != null){
			channel.setUserData(key, value)
		}
		""
	}
	def get_agent_channel(request){
		String agentId = request.getParameter('agent')
		AcdAgent tAgent= AcdManager.getInstance().getAgentById(agentId)	
		def result = new JsonGroovyBuilder().json{
			agent = tAgent.getId()
			channel = tAgent.getChannelId()
			callChannel = tAgent.getCallChannelId()
		}.toString()
		return result
	}
	def hangup_agent(request){
		String agentId = request.getParameter('agentId')
		AcdAgent tAgent = AcdManager.getInstance().getAgentById(agentId)
		if (tAgent != null){
			String channelId = tAgent.getChannelId()
			HangupRequest hangupRequest = new HangupRequest(channelId, DisconnectCode.Normal)
			Switches.getInstance().processRequest(hangupRequest)
			return "Agent(${agentId}) hangup"
		}
		return "Agent ID ${agentId} not found"
	}
	def get_agent_data(request){
		String agentName = request.getParameter('agent')
		String agentId = request.getParameter('agentId')
		String dataKey = request.getParameter('key')
		String requestId = request.getParameter('requestId')
		AcdAgent tAgent = null
		if (agentId)
			tAgent = AcdManager.getInstance().getAgentById(agentId)
		else if (agentName)
			tAgent = AcdManager.getInstance().getAgentByName(agentName)
			
		log.trace(tAgent.getCallChannelId())
		String t_message = "No Error"
		if (tAgent!=null){
			Channel channel = Switches.getInstance().getChannelById(tAgent.getCallChannelId())
			
			if (channel != null){
				if (channel.getUserData(dataKey) != null){
					def result = new JsonGroovyBuilder().json{
						request=requestId
						message=""
						status=0
						key = dataKey
						value = channel?.getUserData(dataKey)
					}.toString()
					return result
				}
				else{
					t_message = "Key ${dataKey} not found in channel ${channel.getUniqueId()}"
				}
			}
			else{
				t_message = "Channel not found"
			}
		}
		return new JsonGroovyBuilder().json{
			requestId=1234
			message=t_message
			status=-1 //TODO put proper status
		}.toString()
	}
	def set_agent_data(request){
		String agentName = request.getParameter('agent')
		String agentId = request.getParameter('agentId')
		String dataKey = request.getParameter('key')
		String requestId = request.getParameter('requestId')
		String dataValue = request.getParameter('value')
		AcdAgent tAgent = null
		if (agentId)
			tAgent = AcdManager.getInstance().getAgentById(agentId)
		else if (agentName)
			tAgent = AcdManager.getInstance().getAgentByName(agentName)
		log.trace(tAgent.getCallChannelId())
		String t_message = "No Error"
		if (tAgent!=null){
			Channel channel = Switches.getInstance().getChannelById(tAgent.getCallChannelId())
			
			if (channel){
				channel.setUserData(dataKey, dataValue)
				t_message = "value set"
			}
			else{
				t_message = "Channel not found"
			}
		}
		return new JsonGroovyBuilder().json{
			requestId=1234
			message=t_message
			status=-1 //TODO put proper status
		}.toString()
	}
	def agent_login(request){
		String agentName = request.getParameter('agent')
		AcdAgent agent = AcdManager.getInstance().getAgentByName(agentName)
		Switch _switch = Switches.getInstance().getRandom();
		String tMessage = "Invalid"
		if (agent != null){
		 if (agent.getPassword().equals(request.getParameter('password'))){
			def result = new JsonGroovyBuilder().json{
				requestId=1234
				message=""
				status=0
				sipProxy= _switch.getDefinition().getAddress();
				sipUsername = agent.getName()
				sipPassword = agent.getPassword()
				sipSecure = false
			}.toString()
			
			return result;
		 }
		 else{
			 tMessage = "Agent ${agentName} Wrong password, entered ${request.getParameter('password')}, got ${agent.getPassword()}"
		 }
		}
		else{
			tMessage = "agent ${agentName} not found"
		}
		return new JsonGroovyBuilder().json{
			requestId=1234
			message=tMessage
			status=-1
		}.toString()
	}
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		
		this.securityProvider = BeansManager.getInstance().getBean(getParameter('SecurityProvider'))
		if (securityProvider.getSecurityLevel(request.getParameter('token')) > 0){
			
			
			def action = request.getParameter("action")
			if (!(action?.length()>0)){
				action = 'index'
			}
			try{			
				log.trace("WebAPI received {"+request+"}");
				def model = "${action}"(request)
				println "model = ${model}"
				response.appendBody(model)
				response.setResponseCode(HttpStatus.ORDINAL_200_OK)
			}	
			catch (Exception e){
				println e
				log.fatal(e.getMessage(), e)
				response.setResponseCode(HttpStatus.Not_Implemented)
			}
		}
		else{
			response.appendBody("Unauthorized")
			response.setResponseCode(HttpStatus.ORDINAL_401_Unauthorized);
		}
	}
}