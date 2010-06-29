package com.admtel.telephonyserver.freeswitch;

import org.apache.mina.core.session.IoSession;

import com.admtel.telephonyserver.config.SwitchListenerDefinition;
import com.admtel.telephonyserver.core.SwitchListener;

public class FSSwitchListener extends SwitchListener {

	public FSSwitchListener(SwitchListenerDefinition definition) {
		super(definition);
		this.encodingDelimiter = "\n\n";
		this.decodingDelimiter = "\n\n";
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		FSSwitch _switch = (FSSwitch) session.getAttribute("Switch");
		if (_switch != null){
			_switch.messageReceived(session, message);
		}
	}

	@Override
	public void afterSessionOpened(IoSession session) {
		session.write("connect");
		
	}
}
