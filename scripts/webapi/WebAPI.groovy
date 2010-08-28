import com.admtel.telephonyserver.core.*;

import java.io.StringWriter;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;

import com.admtel.telephonyserver.httpserver.AdmServlet;

class WebAPI implements AdmServlet {
	
	static Logger log = Logger.getLogger(WebAPI.class)
	
	
	def index(request){
		[index:"welcome"]
	}
	def originate(request){
		log.trace("Originate ${request}")
		Switch _switch = Switches.getInstance().getRandom();
		if (_switch != null){
			_switch.originate(request.getParameter("destination"), 10000, "", "", request.getParameter("script"), "")
			return "dialed ${request.getParameter('destination')}"
		}
		"no switch found"
	}
		
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		

		
		def action = request.getParameter("action")
		if (!(action?.length()>0)){
			action = 'index'
		}
		try{			
			def model = "${action}"(request)
			response.appendBody(model)	
		}	
		catch (Exception e){
			log.fatal(e.getMessage(), e)
			response.appendBody(e.getMessage())
		}
					
	}
}