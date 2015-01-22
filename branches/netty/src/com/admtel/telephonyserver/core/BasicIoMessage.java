package com.admtel.telephonyserver.core;

import org.apache.mina.core.session.IoSession;

public class BasicIoMessage {
	IoSession session;
	String message;
	
	public BasicIoMessage(IoSession session, String message) {
		super();
		this.session = session;
		this.message = message;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "BasicIoMessage ["
				+ (session != null ? "session=" + session + ", " : "")
				+ (message != null ? "message=" + message : "") + "]";
	}
	
}
