package com.admtel.telephonyserver.tests;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.admtel.telephonyserver.core.DisconnectCode;
import com.admtel.telephonyserver.requests.HangupRequest;
import com.admtel.telephonyserver.requests.Request;

public class JSonTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		HangupRequest request = new HangupRequest("12345", DisconnectCode.Normal);
		mapper.enableDefaultTyping(); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE		
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		String requestStr = mapper.writeValueAsString(request); 
		System.out.println(requestStr);
		Request r = mapper.readValue(requestStr,Request.class);
		System.out.println(r);


	}

}
