package com.admtel.telephonyserver.radius;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.SocketException;
import java.util.List;

import org.apache.log4j.Logger;
import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.attribute.VendorSpecificAttribute;
import org.tinyradius.dictionary.Dictionary;
import org.tinyradius.dictionary.DictionaryParser;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusClient;
import org.tinyradius.util.RadiusException;
import org.tinyradius.util.RadiusUtil;

import com.admtel.telephonyserver.config.RadiusDefinition;
import com.admtel.telephonyserver.core.AdmTelephonyServer;
import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.Channel.CallOrigin;
import com.admtel.telephonyserver.interfaces.Authorizer;
import com.admtel.telephonyserver.utils.AdmUtils;

public class RadiusServer implements Authorizer{
	
	public enum AccountingType {Start, Stop, InterimUpdate};
	
	
	static Logger log = Logger.getLogger(RadiusServer.class);

	private static final String DICTIONARY_RESOURCE = "org/tinyradius/dictionary/default_dictionary";
	private static final String ADM_DICTIONARY = "com/admtel/telephonyserver/radius/adm_dictionary";

	public RadiusDefinition definition;

	Dictionary dictionary;

	public RadiusServer(RadiusDefinition definition) {
		this.definition = definition;
		InputStream s1 = RadiusServer.class.getClassLoader()
				.getResourceAsStream(DICTIONARY_RESOURCE);
		InputStream s2 = RadiusServer.class.getClassLoader()
				.getResourceAsStream(ADM_DICTIONARY);
		InputStream s = new SequenceInputStream(s1, s2);
		try {
			dictionary = DictionaryParser.parseDictionary(s);
		} catch (IOException e) {
			log.fatal("Failed to instanciate RadiusServer", e);
		}
	}

	protected RadiusClient getRadiusClient(){
		RadiusClient radiusClient;
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
		return radiusClient;
	}

	@Override
	public AuthorizeResult authorize(Channel channel, String username,
			String password, String address, String calledStationId, boolean routing, boolean number) {
		
		if (channel==null){
			log.warn("channel is null");
			return new AuthorizeResult();
		}
		
		log.trace(String.format("authorize :%s:%s:%s:%s:%s", username, address, calledStationId, routing, number));
		if (username == null || username.isEmpty()){
			username="0000";
		}
		if(password==null || password.isEmpty()){
			password="0000";
		}
		String callingStationId = channel.getCallingStationId();
		if (callingStationId == null || callingStationId.isEmpty()){
			callingStationId="0000";
		}
		if(calledStationId == null || calledStationId.isEmpty()){
			calledStationId="0000";
		}
		
		AccessRequest ar = new AccessRequest(username, password);
		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(ar);
		AuthorizeResult result = new AuthorizeResult();
		//ar.setAuthProtocol(AccessRequest.AUTH_PAP); // or AUTH_CHAP
		ar.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address", AdmTelephonyServer.getInstance().getDefinition().getAddress());
		arDecorator.addAttribute("Service-Type", channel.getServiceType());
		arDecorator.addAttribute("Calling-Station-Id", callingStationId);
		arDecorator.addAttribute("Called-Station-Id", calledStationId);
		arDecorator.addAttribute("Login-IP-Host",channel.getLoginIP());

		
		String xpgkRequestType="";
		if (routing) {
			xpgkRequestType = "route";			
		} 
		if (number) {
			xpgkRequestType +=",number";
		}
		if (xpgkRequestType != null && !xpgkRequestType.isEmpty()){			
			arDecorator.addAttribute("Cisco-AVPair", "xpgk-request-type="+xpgkRequestType);
		}
		
		RadiusPacket response;
		try {
			RadiusClient radiusClient = getRadiusClient();
			response = radiusClient.authenticate(ar);
			response.setDictionary(dictionary);
			
			//log.debug(response);
			switch (response.getPacketType()){
			case RadiusPacket.ACCESS_ACCEPT:{
					result.authorized = true;
					
			
					List<VendorSpecificAttribute> attributes = response.getAttributes(26);
					for (VendorSpecificAttribute attribute:attributes){
						List<RadiusAttribute> subAttributes = attribute.getSubAttributes();
						for (RadiusAttribute attribute2:subAttributes){
							//log.debug(attribute2.getAttributeTypeObject().getName()+"====="+RadiusUtil.getStringFromUtf8(attribute2.getAttributeData()));
							if (attribute2.getAttributeTypeObject().getName().equals("Cisco-Command-Code")){
								result.routes.add(RadiusUtil.getStringFromUtf8(attribute2.getAttributeData()));
							}
							else
							if (attribute2.getAttributeTypeObject().getName().equals("h323-credit-time")){
								try{
									result.allowedTime = Integer.valueOf(RadiusUtil.getStringFromUtf8(attribute2.getAttributeData()));
								}
								catch(Exception e){
									result.allowedTime = 0;
								}
							}
						}
					}
					String tUsername = username;
					try{
						tUsername = response.getAttributeValue("user-name");
					}
					catch (IllegalArgumentException e){
						
					}
					result.setUserName(tUsername);
				}
				break;
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (RadiusException e) {
			log.error(e.getMessage(), e);
		}
		return result;	}

	@Override
	public boolean accountingInterimUpdate(Channel channel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accountingStart(Channel channel) {
		
		log.trace("Sending Accounting-Start message for channel " + channel);
		AccountingRequest acctRequest = new AccountingRequest(channel.getUserName(),
				AccountingRequest.ACCT_STATUS_TYPE_START);
		
		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(acctRequest);
		
		acctRequest.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address", AdmTelephonyServer.getInstance().getDefinition().getAddress());
		arDecorator.addAttribute("Service-Type", channel.getServiceType());
		arDecorator.addAttribute("Calling-Station-Id", channel.getCallingStationId());
		arDecorator.addAttribute("Called-Station-Id", channel.getCalledStationId());
		arDecorator.addAttribute("Acct-Session-Id", channel.getAcctSessionId());
		arDecorator.addAttribute("h323-call-origin",(channel.getCallOrigin()==CallOrigin.Inbound?"answer":"originate"));
		arDecorator.addAttribute("h323-setup-time","h323-setup-time="+AdmUtils.dateToRadiusStr(channel.getSetupTime()));
		arDecorator.addAttribute("Acct-Delay-Time","0");
		arDecorator.addAttribute("NAS-Port-Type","Async");//TODO, set proper value
		arDecorator.addAttribute("Acct-Multi-Session-Id", channel.getAcctUniqueSessionId());
		arDecorator.addAttribute("Login-IP-Host",channel.getLoginIP());
		
		try {
			getRadiusClient().account(acctRequest);
		} catch (IOException e) {
			log.error("", e);
		} catch (RadiusException e) {
			log.error("", e);
		}

		return true;
	}

	@Override
	public boolean accountingStop(Channel channel) {
		
		log.trace("Sending Accounting-Stop message for channel " + channel);
		
		AccountingRequest acctRequest = new AccountingRequest(channel.getUserName(),
				AccountingRequest.ACCT_STATUS_TYPE_STOP);
		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(acctRequest);

		acctRequest.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address", AdmTelephonyServer.getInstance().getDefinition().getAddress());
		arDecorator.addAttribute("Service-Type", channel.getServiceType());
		arDecorator.addAttribute("Calling-Station-Id", channel.getCallingStationId());
		arDecorator.addAttribute("Called-Station-Id", channel.getCalledStationId());
		arDecorator.addAttribute("Acct-Session-Id", channel.getAcctSessionId());
		arDecorator.addAttribute("h323-call-origin",(channel.getCallOrigin()==CallOrigin.Inbound?"answer":"originate"));
		arDecorator.addAttribute("h323-setup-time","h323-setup-time="+AdmUtils.dateToRadiusStr(channel.getSetupTime()));
		arDecorator.addAttribute("Acct-Delay-Time","0");
		arDecorator.addAttribute("Acct-Session-Time",Long.toString(channel.getSessionTime()));
		arDecorator.addAttribute("NAS-Port-Type","Async");//TODO, set proper value
		arDecorator.addAttribute("Acct-Multi-Session-Id", channel.getAcctUniqueSessionId());
		arDecorator.addAttribute("h323-disconnect-cause","h323-disconnect-cause="+channel.getH323DisconnectCause());
		arDecorator.addAttribute("Login-IP-Host",channel.getLoginIP());
		arDecorator.addAttribute("h323-remote-address", channel.getChannelData().getRemoteIP());



		if (channel.getAnswerTime()!=null){
			acctRequest.addAttribute("h323-connect-time","h323-connect-time="+AdmUtils.dateToRadiusStr(channel.getAnswerTime()));
		}
		if (channel.getHangupTime()!=null){
			acctRequest.addAttribute("h323-disconnect-time","h323-disconnect-time="+AdmUtils.dateToRadiusStr(channel.getHangupTime()));
		}
		try {
			getRadiusClient().account(acctRequest);
		} catch (IOException e) {
			log.error("", e);
		} catch (RadiusException e) {
			log.error("", e);
		}
		return true;
	}
	
	/*public void accounting (AccountingType type, String userName, String serviceType, String acctUniqueSessionId, String acctSessionId, 
			int accountDelayTime, String calledStationId, String callingStationId, CallOrigin callOrigin, DateTime setupTime, String gwId, String remoteAddress, ){
		
	}*/

}
