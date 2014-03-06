import java.util.UUID;

import com.admtel.telephonyserver.core.*;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.omg.Dynamic.Parameter;

import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;
import com.admtel.telephonyserver.httpserver.AdmServlet;
import com.admtel.telephonyserver.acd.*;
import com.admtel.telephonyserver.events.*;
import java.util.UUID;
import com.admtel.telephonyserver.misc.*;

import freemarker.template.*;

class WebConfServlet extends AdmServlet {
	
	
	static Configuration config = null;
	static Logger log = Logger.getLogger(WebConfServlet.class)
	public Expando userAuthorizer; //injected value
	
	static{
		config = new Configuration()
		try{
			config.setDirectoryForTemplateLoading(new File("./dynamic_beans/web"))
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
		//TODO result in response
		API_Manager.instance.hangup(request.getParameter('channel'))
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
        def result = Registrar.instance.get()
        log.trace(result.size() +", content:" + result.each{
            it
        })
		['page':'users.ftl', users:Registrar.getInstance().get(params.offset, params.max)]
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def channels(request, response){
		def page = request.getParameter("page")
		if (!page) page = 0
		else
		 page = page as int
		List<Channel> channels =  Switches.getInstance().getWithOffsetAndCount(page*10,10)
		int totalChannels = Switches.getInstance().getChannelCount()
		int pagesAvailable = totalChannels/10 + ((totalChannels%10)>0?1:0)
		def root =["channels":channels, 
			"paginationData":["pageNumber":1,"pageSize":10, "pagesAvailable":pagesAvailable, 
				"sortDirection":"ascending", "sortField":"channels"]]		
		return root
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def scripts(request, response){
		def s = ScriptManager.getInstance().getScripts();
		['scripts':s]
	}
	
	def switches(request, response){
		def switches = Switches.getInstance().getAll();
		['switches':switches]
	}
	def switchStop(request, response){
		def _switch = Switches.getInstance().getById(request.getParameter("id"))
		if (_switch){
			_switch.setStatus(Switch.SwitchStatus.Stopped)
		}
		def switches = Switches.getInstance().getAll();
		[page:'switches.ftl',switches:switches]
	}
	def switchStart(request, response){
		def _switch = Switches.getInstance().getById(request.getParameter("id"))
		if (_switch){
			_switch.setStatus(Switch.SwitchStatus.Started)
		}
		def switches = Switches.getInstance().getAll();
		[page:'switches.ftl',switches:switches]
	}
	def hangupAllChannels(request, response){
		def _switch = Switches.getInstance().getById(request.getParameter("id"))
		if (_switch){
			_switch.hangupAllChannels()
		}
		def switches = Switches.getInstance().getAll();
		[page:'switches.ftl',switches:switches]

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
        ['page':'dial.ftl']
    }
	def dialSubmit(request, response){
        log.trace("DialSubmit ****************")
		String destination = URLDecoder.decode(request.getParameter('destination'))
		String channel = request.getParameter('channel')
		int timeout = Integer.valueOf(request.getParameter('timeout'))
		
		//TODO dial

		return ['page':'dial.ftl', 'dialInstance':'dialed given number']
	}


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    def originate(request, response){
        ['page':'originate.ftl']
    }
    def originateSubmit(request, response){
        log.trace("OriginateSubmit ****************")

        String destination = URLDecoder.decode(request.getParameter('destination'),"UTF-8")
		String script = URLDecoder.decode(request.getParameter('script'), "UTF-8")
		String variables = URLDecoder.decode(request.getParameter('variables'), "UTF-8")
        int timeout = 10000
        if (request.getParameter('timeout')){
            timeout = Integer.valueOf(request.getParameter('timeout'))
        }
        def message = "Originating to: ${destination}"
        if (destination){
			if (variables){
				VariableMap vars = new VariableMap()
				vars.addDelimitedVars(variables,"=",",")
				API_Manager.instance.originate(destination, script,vars, timeout)
			}
			else{
				API_Manager.instance.originate(destination, script,null, timeout)
			}
        }
        else{
            message = "Failed to originate, invalid destination"
        }
        return ['page':'originate.ftl', 'message':message]
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
				if (!action) action = request.getParameter("field") //for pagination, we send the action in the field
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