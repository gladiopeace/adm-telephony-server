import com.admtel.telephonyserver.httpserver.AdmServlet;
import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;

class AsteriskCurlExample implements AdmServlet {
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		request.getHeaders().each{
			println it
		}
	}
}