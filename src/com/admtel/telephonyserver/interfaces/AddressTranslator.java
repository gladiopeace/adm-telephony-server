package com.admtel.telephonyserver.interfaces;

import com.admtel.telephonyserver.core.AdmAddress;

public interface AddressTranslator {
	//sip:<destination>[@<gateway>]
	//iax2:<destination>[@<gateway>]
	//pstn:<destination>
	public String translate(AdmAddress address);
}
