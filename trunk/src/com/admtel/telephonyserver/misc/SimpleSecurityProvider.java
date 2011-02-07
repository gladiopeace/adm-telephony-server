package com.admtel.telephonyserver.misc;

import com.admtel.telephonyserver.interfaces.TokenSecurityProvider;

public class SimpleSecurityProvider implements TokenSecurityProvider {

	public void init(){
		
	}
	@Override
	public Integer getSecurityLevel(String token) {
		if (token == null || token.length()==0) return 0;
		return 1;
	}

}
