package com.admtel.telephonyserver.interfaces;

public interface TokenSecurityProvider {
	Integer getSecurityLevel(String token);
}
