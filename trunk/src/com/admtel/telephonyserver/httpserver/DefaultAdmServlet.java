package com.admtel.telephonyserver.httpserver;

import java.util.Iterator;
import java.util.Map;

public class DefaultAdmServlet extends AdmServlet {

	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response) {
		Map<String, String[]> headers = request.getHeaders();
		 Iterator<Map.Entry<String, String[]>> it = headers.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String,String[]> pairs = it.next();
		        	response.appendBody(pairs.getKey() + " = " );
		        	for (int i =0; i< pairs.getValue().length;i++){
		        		response.appendBody((String)pairs.getValue()[i]);
		        		if ((i+1)<pairs.getValue().length){
		        			response.appendBody(",");
		        		}
		        	}
		        response.appendBody("<br/>");		        
		    }
	}

}
