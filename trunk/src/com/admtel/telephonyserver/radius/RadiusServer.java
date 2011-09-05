package com.admtel.telephonyserver.radius;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.math.BigDecimal;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
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
import com.admtel.telephonyserver.config.SystemConfig;
import com.admtel.telephonyserver.core.AdmTelephonyServer;
import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.Conference;
import com.admtel.telephonyserver.core.Participant;
import com.admtel.telephonyserver.core.CallOrigin;
import com.admtel.telephonyserver.interfaces.Authorizer;
import com.admtel.telephonyserver.utils.AdmUtils;

public class RadiusServer implements Authorizer {

	public enum AccountingType {
		Start, Stop, InterimUpdate
	};

	static Logger log = Logger.getLogger(RadiusServer.class);

	private static final String DICTIONARY_RESOURCE = "org/tinyradius/dictionary/default_dictionary";
	private static final String ADM_DICTIONARY = "com/admtel/telephonyserver/radius/adm_dictionary";

	public RadiusDefinition definition;

	Dictionary dictionary;

	FileOutputStream fos = null;
	BufferedOutputStream bos = null;
	DataOutputStream dos = null;

	final static String CONFERENCE_SERVICE_TYPE = "Conference";

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
		if (definition.isLog()) {
			try {
				fos = new FileOutputStream(new File("radius.log"));
				bos = new BufferedOutputStream(fos);
				dos = new DataOutputStream(bos);
			} catch (IOException e) {
				log.fatal(e.getMessage());
			}
		}
	}

	protected RadiusClient getRadiusClient() {
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
	public AuthorizeResult authorize(String username,
			String password, String address, String serviceType,
			String calledStationId, String callingStationId, String loginIp, String serviceNumber, boolean routing, boolean number) {		

		log.trace(String.format("authorize :%s:%s:%s:%s:%s", username, address,
				calledStationId, routing, number));
		if (username == null || username.isEmpty()) {
			username = "0000";
		}
		if (password == null || password.isEmpty()) {
			password = "0000";
		}		
		if (callingStationId == null || callingStationId.isEmpty()) {
			callingStationId = "0000";
		}
		if (calledStationId == null || calledStationId.isEmpty()) {
			calledStationId = "0000";
		}
		if (loginIp == null || loginIp.isEmpty()){
			loginIp = "127.0.0.1";
		}

		AccessRequest ar = new AccessRequest(username, password);
		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(ar);
		AuthorizeResult result = new AuthorizeResult();
		// ar.setAuthProtocol(AccessRequest.AUTH_PAP); // or AUTH_CHAP
		ar.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address",
				SystemConfig.getInstance().serverDefinition.getAddress());
		arDecorator.addAttribute("NAS-Port-Type", "Async");// TODO, set proper
		// value
		arDecorator.addAttribute("NAS-Port", "0"); //TODO set proper value

		arDecorator.addAttribute("Service-Type", serviceType);
		arDecorator.addAttribute("Calling-Station-Id", callingStationId);
		arDecorator.addAttribute("Called-Station-Id", calledStationId);
		arDecorator.addAttribute("Login-IP-Host", loginIp);
		if (serviceNumber != null) {
			arDecorator.addAttribute("Cisco-AVPair", "access-number="
					+ serviceNumber);
		}

		String xpgkRequestType = "";
		if (routing) {
			xpgkRequestType = "route";
		}
		if (number) {
			xpgkRequestType += ",number";
		}
		if (xpgkRequestType != null && !xpgkRequestType.isEmpty()) {
			arDecorator.addAttribute("Cisco-AVPair", "xpgk-request-type="
					+ xpgkRequestType);
		}

		log.trace("Sending Radius Authorize message : " + ar+"\n\n");
		
		RadiusPacket response;
		try {
			RadiusClient radiusClient = getRadiusClient();
			response = radiusClient.authenticate(ar);
			response.setDictionary(dictionary);

			log.trace(response);
			switch (response.getPacketType()) {
			case RadiusPacket.ACCESS_ACCEPT: {
				result.authorized = true;

				List<VendorSpecificAttribute> attributes = response
						.getAttributes(26);
				for (VendorSpecificAttribute attribute : attributes) {
					List<RadiusAttribute> subAttributes = attribute
							.getSubAttributes();
					for (RadiusAttribute attribute2 : subAttributes) {
						// log.debug(attribute2.getAttributeTypeObject().getName()+"====="+RadiusUtil.getStringFromUtf8(attribute2.getAttributeData()));
						if (attribute2.getAttributeTypeObject().getName()
								.equals("Cisco-Command-Code")) {
							if (result.getRoutes() == null) {
								result.setRoutes(new ArrayList());
							}
							result.getRoutes().add(
									RadiusUtil.getStringFromUtf8(attribute2
											.getAttributeData()));
						} else if (attribute2.getAttributeTypeObject()
								.getName().equals("h323-credit-time")) {
							try {
								result.allowedTime = Integer.valueOf(RadiusUtil
										.getStringFromUtf8(attribute2
												.getAttributeData()));
							} catch (Exception e) {
								result.allowedTime = 0;
							}
						} else if (attribute2.getAttributeTypeObject()
								.getName().equals("h323-credit-amount")) {
							try {
								result.credit = new BigDecimal(RadiusUtil
										.getStringFromUtf8(attribute2
												.getAttributeData()));
							} catch (Exception e) {
								result.credit = BigDecimal.ZERO;
							}
						} else if (attribute2.getAttributeTypeObject()
								.getName().equals("Cisco-AVPair")) {
							String attribute_data = RadiusUtil
									.getStringFromUtf8(attribute2
											.getAttributeData());
							String[] key_value = attribute_data.split("=");
							if (key_value.length == 2) {
								Object o = result.get(key_value[0]);
								if (o != null) {
									if (o instanceof List) {
										List l = (List) o;
										l.add(key_value[1]);
									} else {
										List l = new ArrayList();
										l.add(o);
										l.add(key_value[1]);
										result.put(key_value[0], l);
									}
								} else {
									result.put(key_value[0], key_value[1]);
								}
							}
						}
					}
				}
				String tUsername = username;
				try {
					tUsername = response.getAttributeValue("user-name");
				} catch (IllegalArgumentException e) {

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
		
		//write to log file
		if (dos != null){
			try {
				dos.writeUTF(ar.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.warn(e.getMessage());
			}
		}
		log.trace(result+"\n\n");
		return result;
	}

	public boolean accountingInterimUpdate(Channel channel) {

		String str = channel.getUserName();
		str = channel.getAccountCode();

		String username = (channel
				.getAccountCode() == null ? channel.getUserName() : channel
				.getAccountCode());
		if (username == null){
			username = "XXXXXXXX"; //TODO parameterize 
		}
		AccountingRequest acctRequest = new AccountingRequest(username,
				AccountingRequest.ACCT_STATUS_TYPE_INTERIM_UPDATE);

		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(
				acctRequest);

		acctRequest.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address",
				SystemConfig.getInstance().serverDefinition.getAddress());
		arDecorator.addAttribute("Service-Type", "Login-User");
		arDecorator.addAttribute("Calling-Station-Id", channel
				.getCallingStationId());
		arDecorator.addAttribute("Called-Station-Id", channel
				.getCalledStationId());
		arDecorator.addAttribute("Acct-Session-Id", channel.getAcctSessionId());
		arDecorator.addAttribute("h323-call-origin",
				(channel.getCallOrigin() == CallOrigin.Inbound ? "answer"
						: "originate"));
		arDecorator.addAttribute("h323-setup-time", "h323-setup-time="
				+ AdmUtils.dateToRadiusStr(channel.getSetupTime()));
		arDecorator.addAttribute("Acct-Delay-Time", "0");
		arDecorator.addAttribute("NAS-Port-Type", "Async");// TODO, set proper
		arDecorator.addAttribute("NAS-Port", "0"); //TODO set proper value
		// value
		arDecorator.addAttribute("Acct-Multi-Session-Id", channel
				.getAcctUniqueSessionId());
		arDecorator.addAttribute("Login-IP-Host", channel.getLoginIP());
		if (channel.getServiceNumber() != null) {
			arDecorator.addAttribute("Cisco-AVPair", "access-number="
					+ channel.getServiceNumber());
		}

		if (channel.getAnswerTime() != null) {
			arDecorator.addAttribute("h323-connect-time", "h323-connect-time="
					+ AdmUtils.dateToRadiusStr(channel.getAnswerTime()));
		}

		log.trace("Sending Accounting-Start message : " + acctRequest+"\n\n");

		try {
			getRadiusClient().account(acctRequest);
		} catch (IOException e) {
			log.error("", e);
		} catch (RadiusException e) {
			log.error("", e);
		}

		return true;
	}

	public boolean accountingStart(Channel channel) {
		String username = (channel
				.getAccountCode() == null ? channel.getUserName() : channel
				.getAccountCode());
		if (username == null){
			username = "XXXXXXXX"; //TODO parameterize 
		}
		
		AccountingRequest acctRequest = new AccountingRequest(username, AccountingRequest.ACCT_STATUS_TYPE_START);

		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(
				acctRequest);

		acctRequest.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address",
				SystemConfig.getInstance().serverDefinition.getAddress());
		arDecorator.addAttribute("Service-Type", "Login-User");
		arDecorator.addAttribute("Calling-Station-Id", channel
				.getCallingStationId());
		arDecorator.addAttribute("Called-Station-Id", channel
				.getCalledStationId());
		arDecorator.addAttribute("Acct-Session-Id", channel.getAcctSessionId());
		arDecorator.addAttribute("h323-call-origin",
				(channel.getCallOrigin() == CallOrigin.Inbound ? "answer"
						: "originate"));
		arDecorator.addAttribute("h323-setup-time", "h323-setup-time="
				+ AdmUtils.dateToRadiusStr(channel.getSetupTime()));
		arDecorator.addAttribute("Acct-Delay-Time", "0");
		arDecorator.addAttribute("NAS-Port-Type", "Async");// TODO, set proper
		arDecorator.addAttribute("NAS-Port", "0"); //TODO set proper value
		// value
		arDecorator.addAttribute("Acct-Multi-Session-Id", channel
				.getAcctUniqueSessionId());
		arDecorator.addAttribute("Login-IP-Host", channel.getLoginIP());
		if (channel.getServiceNumber() != null) {
			arDecorator.addAttribute("Cisco-AVPair", "access-number="
					+ channel.getServiceNumber());
		}

		log.trace("Sending Accounting-Start message : " + acctRequest+"\n\n");

		try {
			getRadiusClient().account(acctRequest);
		} catch (IOException e) {
			log.error("", e);
		} catch (RadiusException e) {
			log.error("", e);
		}

		return true;
	}

	public boolean accountingStop(Channel channel) {

		String username = (channel
				.getAccountCode() == null ? channel.getUserName() : channel
				.getAccountCode());
		if (username == null){
			username = "XXXXXXXX"; //TODO parameterize 
		}
		
		AccountingRequest acctRequest = new AccountingRequest(username, AccountingRequest.ACCT_STATUS_TYPE_STOP);
		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(
				acctRequest);

		acctRequest.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address",
				SystemConfig.getInstance().serverDefinition.getAddress());
		arDecorator.addAttribute("Service-Type", "Login-User");
		arDecorator.addAttribute("Calling-Station-Id", channel
				.getCallingStationId());
		arDecorator.addAttribute("Called-Station-Id", channel
				.getCalledStationId());
		arDecorator.addAttribute("Acct-Session-Id", channel.getAcctSessionId());
		arDecorator.addAttribute("h323-call-origin",
				(channel.getCallOrigin() == CallOrigin.Inbound ? "answer"
						: "originate"));
		arDecorator.addAttribute("h323-setup-time", "h323-setup-time="
				+ AdmUtils.dateToRadiusStr(channel.getSetupTime()));
		arDecorator.addAttribute("Acct-Delay-Time", "0");
		arDecorator.addAttribute("Acct-Session-Time", Long.toString(channel
				.getSessionTime()));
		arDecorator.addAttribute("NAS-Port-Type", "Async");// TODO, set proper
		// value
		arDecorator.addAttribute("NAS-Port", "0"); //TODO set proper value
		arDecorator.addAttribute("Acct-Multi-Session-Id", channel
				.getAcctUniqueSessionId());
		arDecorator.addAttribute("h323-disconnect-cause",
				"h323-disconnect-cause=" + channel.getH323DisconnectCause());
		arDecorator.addAttribute("Login-IP-Host", channel.getLoginIP());
		arDecorator.addAttribute("h323-remote-address", channel
				.getChannelData().getRemoteIP());
		arDecorator.addAttribute("xpgk-dst-number-in", channel.getChannelData()
				.getDestinationNumberIn());
		if (channel.getServiceNumber() != null) {
			arDecorator.addAttribute("Cisco-AVPair", "access-number="
					+ channel.getServiceNumber());
		}

		if (channel.getAnswerTime() != null) {
			arDecorator.addAttribute("h323-connect-time", "h323-connect-time="
					+ AdmUtils.dateToRadiusStr(channel.getAnswerTime()));
		}
		if (channel.getHangupTime() != null) {
			arDecorator
					.addAttribute("h323-disconnect-time",
							"h323-disconnect-time="
									+ AdmUtils.dateToRadiusStr(channel
											.getHangupTime()));
		}
		log.trace("Sending Accounting-Stop message : " + acctRequest+"\n\n");

		try {
			getRadiusClient().account(acctRequest);
		} catch (IOException e) {
			log.error("", e);
		} catch (RadiusException e) {
			log.error("", e);
		}
		return true;
	}

	public boolean accountingInterimUpdate(Channel channel,
			Conference conference, Participant participant) {
		AccountingRequest acctRequest = new AccountingRequest(conference
				.getId(), AccountingRequest.ACCT_STATUS_TYPE_INTERIM_UPDATE);

		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(
				acctRequest);

		acctRequest.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address",
				SystemConfig.getInstance().serverDefinition.getAddress());
		arDecorator.addAttribute("Service-Type", CONFERENCE_SERVICE_TYPE);
		arDecorator.addAttribute("Calling-Station-Id", channel
				.getCallingStationId());
		arDecorator.addAttribute("Called-Station-Id", channel
				.getCalledStationId());
		arDecorator.addAttribute("Acct-Session-Id", participant.getUniqueId());
		arDecorator.addAttribute("h323-call-origin",
				(channel.getCallOrigin() == CallOrigin.Inbound ? "answer"
						: "originate"));
		arDecorator.addAttribute("h323-setup-time", "h323-setup-time="
				+ AdmUtils.dateToRadiusStr(channel.getSetupTime()));
		arDecorator.addAttribute("Acct-Delay-Time", "0");
		arDecorator.addAttribute("NAS-Port-Type", "Async");// TODO, set proper
		// value
		arDecorator.addAttribute("NAS-Port", "0"); //TODO set proper value
		arDecorator.addAttribute("Acct-Multi-Session-Id", channel
				.getAcctUniqueSessionId());
		arDecorator.addAttribute("Login-IP-Host", channel.getLoginIP());
		if (channel.getServiceNumber() != null) {
			arDecorator.addAttribute("Cisco-AVPair", "access-number="
					+ channel.getServiceNumber());
		}

		if (participant.getJoinTime() != null) {
			arDecorator.addAttribute("h323-connect-time", "h323-connect-time="
					+ AdmUtils.dateToRadiusStr(participant.getJoinTime()));
		}

		log.trace("Sending Accounting-Start message : " + acctRequest);

		try {
			getRadiusClient().account(acctRequest);
		} catch (IOException e) {
			log.error("", e);
		} catch (RadiusException e) {
			log.error("", e);
		}

		return true;
	}

	public boolean accountingStart(Channel channel, Conference conference,
			Participant participant) {
		AccountingRequest acctRequest = new AccountingRequest(conference
				.getId(), AccountingRequest.ACCT_STATUS_TYPE_START);

		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(
				acctRequest);

		acctRequest.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address",
				SystemConfig.getInstance().serverDefinition.getAddress());
		arDecorator.addAttribute("Service-Type", CONFERENCE_SERVICE_TYPE);
		arDecorator.addAttribute("Calling-Station-Id", channel
				.getCallingStationId());
		arDecorator.addAttribute("Called-Station-Id", channel
				.getCalledStationId());
		arDecorator.addAttribute("Acct-Session-Id", participant.getUniqueId());
		arDecorator.addAttribute("h323-call-origin",
				(channel.getCallOrigin() == CallOrigin.Inbound ? "answer"
						: "originate"));
		arDecorator.addAttribute("h323-setup-time", "h323-setup-time="
				+ AdmUtils.dateToRadiusStr(channel.getSetupTime()));
		arDecorator.addAttribute("Acct-Delay-Time", "0");
		arDecorator.addAttribute("NAS-Port-Type", "Async");// TODO, set proper
		// value
		arDecorator.addAttribute("NAS-Port", "0"); //TODO set proper value
		arDecorator.addAttribute("Acct-Multi-Session-Id", channel
				.getAcctUniqueSessionId());
		arDecorator.addAttribute("Login-IP-Host", channel.getLoginIP());
		if (channel.getServiceNumber() != null) {
			arDecorator.addAttribute("Cisco-AVPair", "access-number="
					+ channel.getServiceNumber());
		}

		log.trace("Sending Accounting-Start message : " + acctRequest);

		try {
			getRadiusClient().account(acctRequest);
		} catch (IOException e) {
			log.error("", e);
		} catch (RadiusException e) {
			log.error("", e);
		}

		return true;
	}

	public boolean accountingStop(Channel channel, Conference conference,
			Participant participant) {
		AccountingRequest acctRequest = new AccountingRequest(conference
				.getId(), AccountingRequest.ACCT_STATUS_TYPE_STOP);
		RadiusPacketDecorator arDecorator = new RadiusPacketDecorator(
				acctRequest);

		DateTime now = new DateTime();
		long sessionTime = new Period(participant.getJoinTime(), now)
				.toStandardSeconds().getSeconds();

		acctRequest.setDictionary(dictionary);
		arDecorator.addAttribute("NAS-IP-Address",
				SystemConfig.getInstance().serverDefinition.getAddress());
		arDecorator.addAttribute("Service-Type", CONFERENCE_SERVICE_TYPE);
		arDecorator.addAttribute("Calling-Station-Id", channel
				.getCallingStationId());
		arDecorator.addAttribute("Called-Station-Id", channel
				.getCalledStationId());
		arDecorator.addAttribute("Acct-Session-Id", participant.getUniqueId());
		arDecorator.addAttribute("h323-call-origin",
				(channel.getCallOrigin() == CallOrigin.Inbound ? "answer"
						: "originate"));
		arDecorator.addAttribute("h323-setup-time", "h323-setup-time="
				+ AdmUtils.dateToRadiusStr(channel.getSetupTime()));
		arDecorator.addAttribute("Acct-Delay-Time", "0");
		arDecorator.addAttribute("Acct-Session-Time", Long
				.toString(sessionTime));
		arDecorator.addAttribute("NAS-Port-Type", "Async");// TODO, set proper
		// value
		arDecorator.addAttribute("NAS-Port", "0"); //TODO set proper value
		arDecorator.addAttribute("Acct-Multi-Session-Id", channel
				.getAcctUniqueSessionId());
		arDecorator.addAttribute("h323-disconnect-cause",
				"h323-disconnect-cause=16");
		arDecorator.addAttribute("Login-IP-Host", channel.getLoginIP());
		arDecorator.addAttribute("h323-remote-address", channel
				.getChannelData().getRemoteIP());
		arDecorator.addAttribute("xpgk-dst-number-in", channel.getChannelData()
				.getDestinationNumberIn());
		if (channel.getServiceNumber() != null) {
			arDecorator.addAttribute("Cisco-AVPair", "access-number="
					+ channel.getServiceNumber());
		}

		if (channel.getAnswerTime() != null) {
			arDecorator.addAttribute("h323-connect-time", "h323-connect-time="
					+ AdmUtils.dateToRadiusStr(participant.getJoinTime()));
		}
		if (channel.getHangupTime() != null) {
			arDecorator.addAttribute("h323-disconnect-time",
					"h323-disconnect-time=" + AdmUtils.dateToRadiusStr(now));
		}
		log.trace("Sending Accounting-Stop message : " + acctRequest);

		try {
			getRadiusClient().account(acctRequest);
		} catch (IOException e) {
			log.error("", e);
		} catch (RadiusException e) {
			log.error("", e);
		}
		return true;
	}

}
