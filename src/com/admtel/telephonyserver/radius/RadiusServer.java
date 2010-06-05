package com.admtel.telephonyserver.radius;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.SocketException;

import org.apache.log4j.Logger;
import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.dictionary.Dictionary;
import org.tinyradius.dictionary.DictionaryParser;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusClient;
import org.tinyradius.util.RadiusException;
import org.tinyradius.util.RadiusUtil;

import com.admtel.telephonyserver.config.RadiusDefinition;
import com.admtel.telephonyserver.core.AdmTelephonyServer;
import com.admtel.telephonyserver.interfaces.Authorizer;

public class RadiusServer implements Authorizer{
	static Logger log = Logger.getLogger(RadiusServer.class);

	private static final String DICTIONARY_RESOURCE = "org/tinyradius/dictionary/default_dictionary";
	private static final String ADM_DICTIONARY = "com/admtel/telephonyserver/radius/adm_dictionary";

	public RadiusDefinition definition;

	RadiusClient radiusClient;
	Dictionary dictionary;

	public RadiusServer(RadiusDefinition definition) {
		this.definition = definition;
		InputStream s1 = RadiusServer.class.getClassLoader()
				.getResourceAsStream(DICTIONARY_RESOURCE);
		InputStream s2 = RadiusServer.class.getClassLoader()
				.getResourceAsStream(ADM_DICTIONARY);
		InputStream s = new SequenceInputStream(s1, s2);
		radiusClient = new RadiusClient(definition.getAddress(), definition
				.getSecret());
		radiusClient.setAcctPort(definition.getAcctPort());
		radiusClient.setAuthPort(definition.getAuthPort());
		radiusClient.setRetryCount(definition.getRetryCount());
		try {
			radiusClient.setSocketTimeout(definition.getSocketTimeout());
		} catch (SocketException e1) {
			log.fatal("Failed to instanciate RadiusServer", e1);
		}
		try {
			dictionary = DictionaryParser.parseDictionary(s);
		} catch (IOException e) {
			log.fatal("Failed to instanciate RadiusServer", e);
		}
	}

	@Override
	public AuthorizeResult authorize(String username,
			String password, String address, String callingStationId,
			String calledStationId, boolean routing, boolean number) {
		log.trace(String.format("authorize :%s:%s:%s:%s:%s:%s", username, address, callingStationId, calledStationId, routing, number));
		if (username == null || username.isEmpty()){
			username="0000";
		}
		if(password==null || password.isEmpty()){
			password="0000";
		}
		if (callingStationId == null || callingStationId.isEmpty()){
			callingStationId="0000";
		}
		if(calledStationId == null || calledStationId.isEmpty()){
			calledStationId="0000";
		}
		
		AccessRequest ar = new AccessRequest(username, password);
		AuthorizeResult result = new AuthorizeResult();
		//ar.setAuthProtocol(AccessRequest.AUTH_PAP); // or AUTH_CHAP
		ar.setDictionary(dictionary);
		ar.addAttribute("NAS-IP-Address", AdmTelephonyServer.getInstance().getDefinition().getAddress());
		ar.addAttribute("Service-Type", "Login-User");
		ar.addAttribute("Calling-Station-Id", callingStationId);
		ar.addAttribute("Called-Station-Id", calledStationId);
		
		String xpgkRequestType="";
		if (routing) {
			xpgkRequestType = "route";			
		} 
		if (number) {
			xpgkRequestType +=",number";
		}
		if (xpgkRequestType != null && !xpgkRequestType.isEmpty()){			
			ar.addAttribute("Cisco-AVPair", "xpgk-request-type="+xpgkRequestType);
		}
		
		RadiusPacket response;
		try {
			response = radiusClient.authenticate(ar);
			response.setDictionary(dictionary);
			log.debug(response);
			switch (response.getPacketType()){
			case RadiusPacket.ACCESS_ACCEPT:{
					result.authorized = true;
					RadiusAttribute attr = response.getAttribute("h323-credit-time");
					if (attr != null){
						try{
							result.allowedTime = Integer.valueOf(RadiusUtil.getStringFromUtf8(attr.getAttributeData()));
						}
						finally{
							result.allowedTime = 0;
						}
							
					}
				}
				break;
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (RadiusException e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

}
