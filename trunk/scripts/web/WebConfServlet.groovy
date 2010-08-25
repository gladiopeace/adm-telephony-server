import com.admtel.telephonyserver.core.*;

import java.io.StringWriter;

import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;

import com.admtel.telephonyserver.httpserver.AdmServlet;

import freemarker.template.*;

class WebConfServlet implements AdmServlet {
	
	
	static Configuration config = null;
	
	static{
		config = new Configuration()
		config.setDirectoryForTemplateLoading(new File("./scripts/webconf"))
	}
	
	def index(request){

		List<Channel> channels =  Switches.getInstance().getAllChannels();
		def root =["channels":channels]
		println request.getParameter("action")
		return root
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
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		

		
		def action = request.getParameter("action")
		if (!(action?.length()>0)){
			action = 'index'
		}
		Template t = config.getTemplate("${action}.ftl")				
		def model = "${action}"(request)
		model['context'] = request.getContext()
		StringWriter writer = new StringWriter()		
		t.process(model, writer)
		response.appendBody(writer.toString())
	}
}