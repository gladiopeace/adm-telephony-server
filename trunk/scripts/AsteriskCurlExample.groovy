import com.admtel.telephonyserver.httpserver.AdmServlet;
import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;

class AsteriskCurlExample extends AdmServlet {
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		request.getHeaders().each{
			println '*********************' + it
		}
		
		println request.getParameter("name")
		println request.getParameter("host")
		
		response.appendBody("user=1000&username=1000&secret=1234&context=adm&host=dynamic&insecure=very&type=friend&fromuser=1000\n\n")
		println response.getBody();
		
				
	}
}