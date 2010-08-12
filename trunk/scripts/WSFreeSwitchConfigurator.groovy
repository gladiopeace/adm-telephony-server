import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;

import com.admtel.telephonyserver.httpserver.AdmServlet;
import groovy.xml.MarkupBuilder;

import groovyx.net.ws.WSClient

class WSFreeSwitchConfigurator implements AdmServlet {
	
	final static String TOKEN = "1234"
	
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){		
		
		def mDomain = request.getParameter("domain")
		def mUser = request.getParameter("user")

		def proxy = new WSClient("http://localhost:8080/adm-appserver/AppServerAPI?WSDL", this.class.classLoader)
		proxy.initialize()
		def subscriberDTO = proxy.getSubscriberByUsername(TOKEN, mUser)
		
		
		
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
										param(name:"password", value:subscriberDTO.password)
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