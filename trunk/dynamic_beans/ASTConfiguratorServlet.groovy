import org.apache.log4j.Logger;

import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;
import com.admtel.telephonyserver.directory.*;
import com.admtel.telephonyserver.httpserver.AdmServlet;
import com.admtel.telephonyserver.interfaces.*;

class ASTConfiguratorServlet extends AdmServlet{
	
	public UserDAO userDAO;
	public GatewayDAO gatewayDAO;
	
	/*
	 * Pull configuration for asterisk
	 * To configure asterisk:
	 * 	in extconfig.conf : sippeers=curl,http://<SERVER>//asterisk-curl
	 * 
	 * 
	 * Don't forget to enable agi in manager.conf
	 * read=system,call,agent,user,config,dtmf,reporting,cdr,dialplan,agi
		write = system,call,agent,user,config,command,reporting,originate,agi
	 * 
	 * */
	static Logger log = Logger.getLogger(ASTConfiguratorServlet.class)
	public void init(){
	}
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){	
		
		String name = URLDecoder.decode(request.getParameter("name"), 'UTF-8')
		String domain = URLDecoder.decode(request.getParameter("URI"), 'UTF-8')		
		User u = userDAO.getUser(name)
		if (u){			
			response.appendBody("defaultuser=${name}&secret=${u.password}&context=internal&host=dynamic&insecure=port"+
				"&type=friend&accountcode=${u.account}&callerid=${u.callerId}&nat=auto_comedia&transport=tcp,udp\n\n")
		}
		else{
			Gateway gateway = gatewayDAO.findById(name)
			if (gateway) {
				def resp = "username=${gateway.username}&defaultuser=${gateway.username}&fromuser=${gateway.username}"+
					"&secret=${gateway.password}&host=${gateway.address}&port=${gateway.port}&type=friend"
				if (gateway.codecs) {
					resp +="&disallow=all"
					gateway.codecs.split(",").each{
						resp += "&allow=${it.trim()}"
					}
				}
				response.appendBody("$resp\n\n")
				log.trace("************** $resp")
			}
			else {
				response.appendBody("error")
			}
		}
	}
} 