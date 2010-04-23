package com.admtel.telephonyserver.core;


import org.apache.mina.core.session.IoSession;

import com.admtel.telephonyserver.config.SwitchListenerDefinition;

public class ASTSwitchListener extends SwitchListener {

	public ASTSwitchListener(SwitchListenerDefinition definition) {
		super(definition);
		this.encodingDelimiter = "\n";
		this.decodingDelimiter = "\n\n";
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		ASTSwitch _switch = (ASTSwitch) session.getAttribute("Switch");
		if (_switch != null){
			_switch.messageReceived(session, message);
		}
	}

	@Override
	public void afterSessionOpened(IoSession session) {
		// TODO Auto-generated method stub
		
	}

}
