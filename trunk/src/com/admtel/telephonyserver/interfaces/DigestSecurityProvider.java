package com.admtel.telephonyserver.interfaces;

public interface DigestSecurityProvider {
	Integer getSecurityLevel(String username, String password);
}
