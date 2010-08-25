import groovyx.net.ws.WSServer
import com.admtel.telephonyserver.core.*
import com.admtel.telephonyserver.events.*
import com.admtel.telephonyserver.radius.*;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.prompts.*;


class ATSService{
	String ping(String toPing){
		return toPing
	}
}

def server = new WSServer()



//server.setNode("ATSService", "http://localhost:6980/ATSService")
		
server.start()
println "AtsWS started"


