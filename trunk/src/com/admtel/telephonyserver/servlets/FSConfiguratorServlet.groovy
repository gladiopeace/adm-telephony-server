package com.admtel.telephonyserver.servlets;

import org.apache.log4j.Logger;
import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;
import com.admtel.telephonyserver.httpserver.AdmServlet;
import groovy.xml.MarkupBuilder;
import com.admtel.telephonyserver.directory.*;
import com.admtel.telephonyserver.interfaces.*;



class FSConfiguratorServlet extends AdmServlet {
	static Logger log = Logger.getLogger(FSConfiguratorServlet.class)

	public UserDAO userDAO;
	public GatewayDAO gatewayDAO;

	public void init(){
	}

	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){

		String pAction = request.getParameter('action')
		if (pAction=='sip_auth'){
			def mDomain = request.getParameter("domain")
			def mUser = request.getParameter("sip_auth_username")
			def mRealm = request.getParameter("sip_auth_realm")

			User u = userDAO.getUser(mUser)
			log.trace("Looking up user ${mUser} in realm : ${mRealm}  for domain :${mDomain} result ${u}")
			def writer = new StringWriter()
			def xml = new MarkupBuilder(writer)

			if (u != null){
				xml.'document'(type: "freeswitch/xml") {
					section(name:"directory"){
						domain(name:mDomain){
							params{
								param(name:"dial-string", value:'{presence_id=${mUser}@${mDomain}}${sofia_contact(${mUser}@${mDomain})}')
							}
							groups{
								group(name:"default"){
									users{
										user(id:mUser){
											params{
												param(name:"password", value:u.getPassword())
											}
											variables{
												variable(name:'accountcode', value:u.getAccount())
												variable(name:'effective_caller_id_number', value:u.getCallerId())
												variable(name:'sip_contact_user', value:u.getCallerId())
											}
										}
									}
								}
							}
						}
					}
				}
			}
			else{
				xml.'document'(type:"freeswitch/xml"){
					section(name:"result"){ result(satus:"not found") }
				}
			}

			log.trace(writer.toString())
			response.appendBody(writer.toString())
		}
		else{
			def writer = new StringWriter()
			def xml = new MarkupBuilder(writer)
			xml.'document'(type:"freeswitch/xml"){
				section(name:"result"){ result(satus:"not found") }
			}
			response.appendBody(writer.toString())			
		}
	}
}