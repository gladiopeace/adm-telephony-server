import com.admtel.telephonyserver.core.Channel
import com.admtel.telephonyserver.events.*;
import com.admtel.telephonyserver.interfaces.EventListener;
import groovy.sql.Sql;
import com.admtel.telephonyserver.events.Event.EventType;
import com.admtel.telephonyserver.core.*;


class CdrListener implements EventListener {
        public String dbUrl
        public String dbUser
        public String dbPassword
        public String dbDriver
        def sql
        def random = new Random()

        public init(){
        println "***********************************"
        /*
                sql = Sql.newInstance(dbUrl, dbUser,
                                dbPassword, dbDriver)
        */
                EventsManager.getInstance().addEventListener ("CdrListener_"+this, this)
        }
        @Override
        public boolean onEvent(Event event) {
                println "***************** " + event
                switch (event.getEventType()){
                        case EventType.Disconnected:
                                DisconnectedEvent de = event
								println "************************* ${de.channel.id} : ${de.channel.calledStationId} : ${de.channel.callingStationId} : ${de.disconnectCode} : setupTimer(${de.channel.setupTime}) : answerTimer(${de.channel.answerTime}) hangupTimer:${de.channel.hangupTime})"                                

                                break;
                }
                return false;
        }
}