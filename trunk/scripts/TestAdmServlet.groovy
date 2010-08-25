import com.admtel.telephonyserver.httpserver.*;

class TestAdmServlet implements AdmServlet {
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){		


		response.appendBody("I am the test servlet")	

	}
}