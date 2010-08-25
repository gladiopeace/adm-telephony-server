import com.admtel.telephonyserver.httpserver.*;

class TestAdmServlet implements AdmServlet {
	
	@Override
	public Object process(HttpRequestMessage request){		

		HttpResponseMessage response = new HttpResponseMessage();
		response.setContentType("text/html");
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);

		response.appendBody("I am the test servlet")	
		
		return response
		
	}
}