package com.admtel.telephonyserver.servlets;

import org.apache.log4j.Logger;
import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;
import com.admtel.telephonyserver.directory.*;
import com.admtel.telephonyserver.httpserver.AdmServlet;
import com.admtel.telephonyserver.interfaces.*;
import com.admtel.telephonyserver.utils.AdmUtils;

class ASTConfiguratorServlet extends AdmServlet{

	public UserDAO userDAO;
	public GatewayDAO gatewayDAO;
	public IpListDAO ipListDAO;

	/*
	 * Pull configuration for asterisk
	 * To configure asterisk:
	 * 	in extconfig.conf : sippeers=curl,http://<SERVER>//asterisk-curl
	 * 
	 * 
	 * Don't forget to enable agi in manager.conf
	 * read=system,call,agent,user,config,dtmf,reporting,cdr,dialplan,agi
	 write = system,call,agent,user,config,command,reporting,originate,agi
	 * 
	 * */
	static Logger log = Logger.getLogger(ASTConfiguratorServlet.class)
	public void init(){
	}

	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){

		String name = URLDecoder.decode(request.getParameter("name"), 'UTF-8')
		String domain = URLDecoder.decode(request.getParameter("URI"), 'UTF-8')
		String host = URLDecoder.decode(request.getParameter("host"), 'UTF-8')
		String callbackextension = URLDecoder.decode(request.getParameter("callbackextension"), 'UTF-8')
		
		if (callbackextension && callbackextension.length() > 0 ) {
			response.appendBody("error\n\n")
			return;
		}

		//		   nat = no                ; Do no special NAT handling other than RFC3581
		//		   nat = force_rport       ; Pretend there was an rport parameter even if there wasn't
		//		   nat = comedia           ; Send media to the port Asterisk received it from regardless
		//		                           ; of where the SDP says to send it.
		//		   nat = auto_force_rport  ; Set the force_rport option if Asterisk detects NAT (default)
		//		   nat = auto_comedia      ; Set the comedia option if Asterisk detects NAT
		//		 The nat settings can be combined. For example, to set both force_rport and comedia
		//		 one would set nat=force_rport,comedia. If any of the comma-separated options is 'no',
		//		 Asterisk will ignore any other settings and set nat=no. If one of the "auto" settings
		//		 is used in conjunction with its non-auto counterpart (nat=comedia,auto_comedia), then
		//		 the non-auto option will be ignored.
		if (host && AdmUtils.validateIP(host)) {
			if (ipListDAO) {
				String accountCode = ipListDAO.accountCode(host)
				if (accountCode) {
					response.appendBody("name=$host&&defaultuser=$host&&context=internal&host=$host&insecure=port"+
						"&type=peer&accountcode=byip&transport=tcp,udp&canreinvite=no\n\n")
				}
			}
		}
		else {
			User u = userDAO.getUser(name)
			if (u){
				response.appendBody("defaultuser=${name}&secret=${u.password}&context=internal&host=dynamic&insecure=port"+
						"&type=friend&accountcode=${u.account}&callerid=${u.callerId}&nat=force_rport,comedia&transport=tcp,udp&canreinvite=no\n\n")
			}
			else{
				Gateway gateway = gatewayDAO.findById(name)				
				if (gateway) {
					def resp
					if (gateway.address){
						if (gateway.username){
							resp = "username=${gateway.username}&defaultuser=${gateway.username}"+
								"&secret=${gateway.password}&host=${gateway.address}&port=${gateway.port}&type=friend&canreinvite=no"
						}
						else{
							resp = "host=${gateway.address}&port=${gateway.port}&type=friend&canreinvite=no"

						}
					}
					else{
						resp = "username=${gateway.username}&defaultuser=${gateway.username}"+
						"&secret=${gateway.password}&host=dynamic&type=friend&canreinvite=no&insecure=port"
					}
					
					if (gateway.codecs) {
						resp +="&disallow=all"
						gateway.codecs.split(",").each{ resp += it.trim()?"&allow=${it.trim()}":""}
					}
					else{
						resp +="&disallow=all&allow=g729&allow=gsm"
					}
					log.trace(resp)
					response.appendBody("$resp\n\n")
				}
				else {
					response.appendBody("error\n\n")
				}
			}
		}
	}
}