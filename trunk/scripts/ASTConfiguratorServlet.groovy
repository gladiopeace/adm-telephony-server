import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;
import com.admtel.telephonyserver.directory.*;
import com.admtel.telephonyserver.httpserver.AdmServlet;

class ASTConfiguratorServlet extends AdmServlet{
	public UserDAO userDAO;
	
	public void init(){
	}
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		String name = request.getParameter("name")
		String domain = request.getParameter("URI")
		println "Got ${name}:${domain}"
		User u = userDAO.getUser(name, domain)
		if (u){
			response.appendBody("user=${name}&username=${name}&secret=${u.getPassword()}&context=adm&host=dynamic&insecure=very&type=friend&fromuser=${name}\n\n")
		}
		else{
			response.appeendBody("error")
		}
	}
} 