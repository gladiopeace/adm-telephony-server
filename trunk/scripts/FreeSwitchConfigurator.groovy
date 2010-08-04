import com.admtel.telephonyserver.httpserver.HttpRequestMessage;
import com.admtel.telephonyserver.httpserver.HttpResponseMessage;

import com.admtel.telephonyserver.httpserver.AdmServlet;
import groovy.xml.MarkupBuilder;
import groovy.sql.Sql



class FreeSwitchConfigurator implements AdmServlet {
	@Override
	public void process(HttpRequestMessage request, HttpResponseMessage response){		
		
		def mDomain = request.getParameter("domain")
		def mUser = request.getParameter("user")
		def sql = Sql.newInstance("jdbc:postgresql://localhost:5432/adm_appserver", "tester",
				"tester1234", "org.postgresql.Driver")
		
		sql.eachRow("select * from subscriber where subscriber.extension=? and subscriber.domain=?",
				[mUser,mDomain], 
				
				
				{ 
					println it
					def sip_domain = request.getParameter("domain")
					def writer = new StringWriter()
					def xml = new MarkupBuilder(writer)
					def subscriber = it
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
													param(name:"password", value:subscriber.password)
												}
											}
										}
									}
								}
							}
						}
					}
					response.appendBody(writer.toString())		
				} );
		
	}
}