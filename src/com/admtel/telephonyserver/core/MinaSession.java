package com.admtel.telephonyserver.core;

import org.apache.mina.core.session.IoSession;

public class MinaSession implements Session{
	
	IoSession session;

	public MinaSession (IoSession session){
		this.session = session;
	}
	@Override
	public void write(String message) {
		session.write(message);		
	}

}
