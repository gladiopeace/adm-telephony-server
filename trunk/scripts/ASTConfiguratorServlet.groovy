import org.apache.log4j.Logger;

import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;
import com.admtel.telephonyserver.directory.*;
import com.admtel.telephonyserver.httpserver.AdmServlet;

class ASTConfiguratorServlet extends AdmServlet{
	
	public UserDAO userDAO;
	
	static Logger log = Logger.getLogger(ASTConfiguratorServlet.class)
	public void init(){
	}
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		String name = request.getParameter("name")
		String domain = request.getParameter("URI")
		log.trace("Got ${name}:${domain}")
		User u = userDAO.getUser(name)
		if (u){			
			response.appendBody("defaultuser=${name}&secret=${u.password}&context=default&host=dynamic&insecure=port"+
				"&type=friend&accountcode=${u.account}&callerid=${u.callerId}&nat=force_rport\n\n")
		}
		else{
			response.appendBody("error")
		}
	}
} 