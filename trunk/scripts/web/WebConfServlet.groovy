import com.admtel.telephonyserver.core.*;

import java.io.StringWriter;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;

import com.admtel.telephonyserver.httpserver.AdmServlet;

import freemarker.template.*;

class WebConfServlet implements AdmServlet {
	
	
	static Configuration config = null;
	static Logger log = Logger.getLogger(WebConfServlet.class)
	
	static{
		config = new Configuration()
		try{
			config.setDirectoryForTemplateLoading(new File("./scripts/web"))
		}
		catch (Exception e){
			log.fatal(e.getMessage(), e)
		}
	}
	
	def index(request){
		[index:"welcome"]
	}
	
	def hangup(request){
		Switch _switch = Switches.getInstance().getById(request.getParameter("switch"))
		if (_switch != null){
			Channel channel = _switch.getChannel(request.getParameter("channel"))
			if (channel != null){
				channel.hangup(DisconnectCode.Normal)
			}
		} 
		[m:'']
	}
	def conferences(request){
		def c = ConferenceManager.getInstance().getAll()
		['conferences':c]
	}
	def channels(request){
		List<Channel> channels =  Switches.getInstance().getAllChannels();
		def root =["channels":channels]
		println request.getParameter("action")
		return root
	}
	def scripts(request){
		def s = Scripts.getInstance().getAll()
		['scripts':s]
	}
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		

		
		def action = request.getParameter("action")
		if (!(action?.length()>0)){
			action = 'index'
		}
		try{
			Template t = config.getTemplate("${action}.ftl")			
			def model = "${action}"(request)
			model['context'] = request.getContext()
			StringWriter writer = new StringWriter()
			t.process(model, writer)
			response.appendBody(writer.toString())	
		}	
		catch (Exception e){
			log.fatal(e.getMessage(), e)
			response.appendBody(e.getMessage())
		}
					
	}
}