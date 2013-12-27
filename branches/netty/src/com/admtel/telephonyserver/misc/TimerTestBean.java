package com.admtel.telephonyserver.misc;

import com.admtel.telephonyserver.core.API_Manager;
import com.admtel.telephonyserver.core.Timers;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import org.apache.log4j.Logger;
import com.admtel.telephonyserver.misc.VariableMap;

/**
 * Created with IntelliJ IDEA.
 * User: danny
 * Date: 5/1/13
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimerTestBean implements TimerNotifiable{
    Logger log = Logger.getLogger(TimerTestBean.class);
    public TimerTestBean(){
        Timers.getInstance().startTimer(this, 1000, false, null);
    }
    public void init(){

    }
    @Override
    public boolean onTimer(Object data) {
        log.trace("Got timer, Originating");
        VariableMap variables = new VariableMap();
        variables.addVariable("number", "1321");
        API_Manager.getInstance().originate("sip:13106998711@asterisk_osx", "ClickToCall/ClickToCallScript.groovy", variables, 1000);
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
