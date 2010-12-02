import org.mortbay.jetty.HttpStatus;

import com.admtel.telephonyserver.core.*;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import com.admtel.telephonyserver.requests.*;
import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;
import groovy.xml.MarkupBuilder;
import com.admtel.telephonyserver.httpserver.AdmServlet;

class WebAPI implements AdmServlet {
	
	static Logger log = Logger.getLogger(WebAPI.class)
	
	
	def index(request){
		println "*********** index"
		"Welcome"
	}
	def hangup(request){
		
		HangupRequest hangupRequest = new HangupRequest(request.getParameter('channel'), DisconnectCode.Normal) 
		Switches.getInstance().processRequest(hangupRequest)
		"Channel hangup request " + request.getParameters('channel')
	}	

	def participant_disconnect(request){

	}
	def conference_details(request){
		Conference c = ConferenceManager.getInstance().getConferenceById(request.getParameter('conference'))
		def writer = new StringWriter()
		if (c != null){
			List<Participant> p = c.getParticipants()
			
			
			def xml = new MarkupBuilder(writer)
			
			xml.'document'(type: "conference/xml") {
				conference(id:c.){
					participants(){
						p.each{
							participant(it.uniqueId)
						}
					}
				}
			}
		}
		writer.toString()			
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
			println "model = ${model}"
			response.appendBody(model)	
		}	
		catch (Exception e){
			println e
			log.fatal(e.getMessage(), e)
			response.setResponseCode(HttpStatus.Not_Implemented)
		}
		response.setResponseCode(HttpStatus.ORDINAL_200_OK)
	}
}