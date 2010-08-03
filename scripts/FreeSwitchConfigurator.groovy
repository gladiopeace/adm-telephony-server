import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;

import com.admtel.telephonyserver.httpserver.AdmServlet;
import groovy.xml.MarkupBuilder;

class FreeSwitchConfigurator implements AdmServlet
{
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){
		println request
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
							user(id:"6000"){
								params{
									param(name:"password", value:"1234")
								}
							}
						}
					}
				}
			}
		  }
		}
		println writer.toString()
		response.appendBody(writer.toString())
	}
}