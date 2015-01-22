package com.admtel.telephonyserver.core;

import org.apache.mina.core.session.IoSession;

public class BasicIoMessage {
	Session session;
	String message;
	
	public BasicIoMessage(Session session, String message) {
		super();
		this.session = session;
		this.message = message;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
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
