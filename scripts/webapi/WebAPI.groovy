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
		Switch _switch = Switches.getInstance().getRandom();
		if (_switch != null){
			_switch.originate(request.getParameter("destination"), 10000, "", "", request.getParameter("script"), "")
			return "dialed ${request.getParameter('destination')}"
		}
		"no switch found"
	}
	def hangup(request){
		String switchId = request.getParameter("switch");
		Switch _switch = Switches.getInstance().getById(request.getParameter("switch"));
		if (_switch == null){
			"Switch ${switchId} not found"
		}
		else{
			String channelId = request.getParameter("channel")
			Channel c = _switch.getChannel(channelId);
			if (c == null){
				"Channel ${channelId} not found"
			}
			else{
				c.hangup(DisconnectCode.Normal)				
			}
		}
	}	
	def hangup(request){

	}
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		

		
		def action = request.getParameter("action")
		if (!(action?.length()>0)){
			action = 'index'
		}
		try{			
			log.trace("WebAPI received {"+request+"}");
			def model = "${action}"(request)
			response.appendBody(model)	
		}	
		catch (Exception e){
			log.fatal(e.getMessage(), e)
			response.appendBody(e.getMessage())
		}
					
	}
}