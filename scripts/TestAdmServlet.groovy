import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;

import com.admtel.telephonyserver.httpserver.AdmServlet;

class WSFreeSwitchConfigurator implements AdmServlet {
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){		
		
		response.appendBody("I am the test servlet")		
		
	}
}