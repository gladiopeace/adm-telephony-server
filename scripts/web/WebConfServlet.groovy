import java.util.UUID;

import com.admtel.telephonyserver.core.*;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.omg.Dynamic.Parameter;

import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;
import com.admtel.telephonyserver.requests.*;
import com.admtel.telephonyserver.httpserver.AdmServlet;
import com.admtel.telephonyserver.acd.*;
import com.admtel.telephonyserver.events.*;
import java.util.UUID;

import freemarker.template.*;

class WebConfServlet extends AdmServlet {
	
	
	static Configuration config = null;
	static Logger log = Logger.getLogger(WebConfServlet.class)
	public Expando userAuthorizer; //injected value
	
	static{
		config = new Configuration()
		try{
			config.setDirectoryForTemplateLoading(new File("./scripts/web"))
		}
		catch (Exception e){
			log.fatal(e.getMessage(), e)
		}
	}
	public init(){
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def index(request, response){
		int cc = Switches.getInstance().channels.size()
		int sc = ScriptManager.getInstance().scripts.size()
		[message:"Channels($cc), Scripts($sc)"]
		//['scriptsCount':sc, 'channelsCount':cc]
	}
	def reload(request, response){
		BeansManager.getInstance().reload();
		ScriptManager.getInstance().reload();
		
		['page':'index.ftl','message':'Reload completed ...']
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def hangup(request, response){
		HangupRequest hangupRequest = new HangupRequest(request.getParameter('channel'), DisconnectCode.Normal) 
		Switches.getInstance().processRequest(hangupRequest)
		[m:'']
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def conferences(request, response){
		def c = ConferenceManager.getInstance().getAll()
		['conferences':c]
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def login(request, response){
		def userName = request.getParameter('username')
		def userPassword =request.getParameter('password')
		def user = userAuthorizer.getUser(userName)
		log.trace("Got user : " + user)
		if (user && user.password == userPassword){
			def sessionId = UUID.randomUUID().toString()
			log.trace("Setting session with ID ${sessionId}")			
			setSession(response,sessionId, new Expando());
			return [page:'index.ftl', message:"welcome ${userName}"]
		}
		['page':'login.ftl']
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def users(request, response){
		def params = request.getParameterMap()
		log.trace("Users with request params : " + params)
		if (!params.max) params.max = 25 
		if (!params.offset) params.offset = 0
		['page':'users.ftl', users:Registrar.getInstance().get(params.offset, params.max)]
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def channels(request, response){
		List<Channel> channels =  Switches.getInstance().getAllChannels();
		def root =["channels":channels]
		println request.getParameter("action")
		return root
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def scripts(request, response){
		def s = ScriptManager.getInstance().getScripts();
		['scripts':s]
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def queues(request, response){
		AcdQueue[] q = AcdManager.getInstance().getQueues();
		['queues':q]
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def queue_calls(request, response){
		String queue = request.getParameter('queue')
		AcdCall[] c = AcdManager.getInstance().getQueueCalls(queue)
		['queue_calls':c]
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def dial(request, response){
		String destination = URLDecoder.decode(request.getParameter('destination'))
		String channel = request.getParameter('channel')
		int timeout = Integer.valueOf(request.getParameter('timeout'))
		DialRequest dialRequest = new DialRequest( channel, destination, timeout)
		Switches.getInstance().processRequest(dialRequest)
		return [m:"Dial ${request.getParameter('destination')}"]
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def channel(request, response){
		String channelId=request.getParameter('id')
		Channel c = Switches.getInstance().getChannelById(channelId)
		return [channel:c]
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def script(request, response){
		String scriptId = request.getParameter('id')
		Script s = ScriptManager.getInstance().getScript(scriptId)
		[script:s]
	}
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		def sessionId = request.getParameter("session")
		log.trace("Got session ${sessionId}")
		def action = 'login'
		if (sessionId){
			def session = getSession(sessionId)
			if(session){
				action = request.getParameter("action")
				if (!(action?.length()>0)){
					action = 'index'
				}		
			}
		}
		try{						
			def model = "${action}"(request, response)
			def page = "${action}.ftl"
			if (model['page']){
				page = model['page']
			}
			Template t = config.getTemplate(page)
			StringWriter writer = new StringWriter()
			t.process(model, writer)			
			response.appendBody(writer.toString())
		}	
		catch (Exception e){
			log.fatal(e.getMessage())
			response.appendBody("Error processing request")
		}
					
	}
}