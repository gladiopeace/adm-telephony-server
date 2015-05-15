package com.admtel.telephonyserver.core;

import java.util.Map.Entry;

import com.admtel.telephonyserver.misc.VariableMap;

public class ChannelData extends VariableMap {
	
	public final static String CALLER_ID_NAME = "CallerIdName";
	public final static String CALLER_ID_NUMBER="CallerIdNumber";
	public final static String CALLED_NUMBER="CalledNumber";
	public final static String USERNAME="UserName";
	public final static String SERVICE_NUMBER="ServiceNumber";
	public final static String REMOTE_IP="remote_ip";
	public final static String GW_ID="GW_ID";
	public final static String LOGIN_IP_HOST = "Login-IP-Host";
	public final static String DESTINATION_NUMBER_IN="DESTINATION_NUMBER_IN";
	private static final String DIALED_CHANNEL = "DIALED_CHANNEL";
	private static final String DIALED_NUMBER = "DIALED_NUMBER";
	private static final String DIALED_IP = "DIALED_IP";
	private static final String SIP_IP = "SIP_IP";
	private static final String SIP_PORT = "SIP_PORT";
	
	public void setDialedChannel(String dialedChannel){
		addVariable(DIALED_CHANNEL, dialedChannel);
	}
	public String getDialedChannel(){
		return getVariable(DIALED_CHANNEL);
	}
	public void setDialedNumber(String dialedNumber){
		addVariable(DIALED_NUMBER, dialedNumber);
	}
	public String getDialedNumber(){
		return getVariable(DIALED_NUMBER);
	}
	public void setDialedIP(String dialedIP){
		addVariable(DIALED_IP, dialedIP);
	}
	public String getDialedIP(){
		return getVariable(DIALED_IP);
	}
	
	public void setCallerIdName(String callerIdName){
		addVariable(CALLER_ID_NAME, callerIdName);
	}
	public String getCallerIdName(){
		return getVariable(CALLER_ID_NAME);
	}
	public void setCallerIdNumber(String callerIdNumber){
		addVariable(CALLER_ID_NUMBER, callerIdNumber);
	}
	public String getCallerIdNumber(){
		return getVariable(CALLER_ID_NUMBER);
	}
	public void setCalledNumber(String calledNumber){
		addVariable(CALLED_NUMBER, calledNumber);
	}
	public String getCalledNumber(){
		return getVariable(CALLED_NUMBER);
	}
	public String toString(){
		String result = super.toString();
		
		for (Entry<String, String> entry:entrySet()){
			result +=","+entry.getKey()+":"+entry.getValue();
		}
		return result;
	}
	public void setUserName(String userName) {
		addVariable(USERNAME, userName);
		
	}
	public String getUserName(){
		return getVariable(USERNAME);
	}
	public void setServiceNumber(String serviceNumber){
		addVariable(SERVICE_NUMBER, serviceNumber);
	}
	public String getServiceNumber(){
		return getVariable(SERVICE_NUMBER);
	}
	public String getRemoteIP() {
		return getVariable(REMOTE_IP);
	}
	public void setLoginIP(String variable) {
		 addVariable(LOGIN_IP_HOST, variable);		
	}
	public String getLoginIP() {
		return getVariable(LOGIN_IP_HOST);
	}
	public void setRemoteIP(String variable) {
		addVariable(REMOTE_IP, variable);
		
	}
	public void setDestinationNumberIn(String calledNumber) {
		addVariable(DESTINATION_NUMBER_IN, calledNumber);
		
	}
	public String getDestinationNumberIn() {
		return getVariable(DESTINATION_NUMBER_IN);
	}
	
	public void setSipIP(String ip){
		addVariable(SIP_IP, ip);
	}
	public String getSipIP(){
		return getVariable(SIP_IP);
	}
	public void setSipPort(int port){
		addVariable(SIP_PORT, Integer.toString(port));
	}
	public int getSipPort(){
		int result = 5060;
		try
		{
			result = Integer.parseInt(getVariable(SIP_PORT));
		}
		catch (Exception e){
			
		}
		return result;
	}
}
