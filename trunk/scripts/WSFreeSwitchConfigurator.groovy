
import com.admtel.telephonyserver.httpserver.*;

import com.admtel.telephonyserver.httpserver.AdmServlet;
import groovy.xml.MarkupBuilder;

import groovyx.net.ws.WSClient

import org.apache.log4j.Logger

class WSFreeSwitchConfigurator implements AdmServlet {
	
	final static String TOKEN = "1234"
	
	final static Logger log = Logger.getLogger(WSFreeSwitchConfigurator.class);
	
	static ThreadLocal wsClients = new ThreadLocal();
	
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){		
				
		def mDomain = request.getParameter("domain")
		def mUser = request.getParameter("user")

		def proxy = wsClients.get()
		log.trace ("Request start ...."+Thread.currentThread())		
		if (proxy==null){
			proxy = new WSClient("http://localhost:8080/adm-appserver/AppServerAPI?WSDL", this.class.classLoader)
			log.trace("WSClient Created")
			proxy.initialize()
			log.trace("proxy initialized")
			wsClients.set(proxy)
		}
		def userDTO = proxy.getUserByName(TOKEN, mUser)
		if (userDTO == null){
			log.warn("Couldn't find user ${mUser}")
			 return null;
		}
		
		log.trace("received result ${userDTO.username}:${userDTO.password}")
		
		
		
		def sip_domain = request.getParameter("domain")
		def writer = new StringWriter()
		def xml = new MarkupBuilder(writer)
		
		xml.'document'(type: "freeswitch/xml") {
			section(name:"directory"){
				domain(name:sip_domain){
					params{
						param(name:"dial-string", value:'{presence_id=${dialed_user}@${dialed_domain}}${sofia_contact(${dialed_user}@${dialed_domain})}')
					}
					groups{
						group(name:"default"){
							users{
								user(id:mUser){
									params{
										param(name:"password", value:userDTO.password)
									}
								}
							}
						}
					}
				}
			}
		}
		response.appendBody(writer.toString())	
	}
}