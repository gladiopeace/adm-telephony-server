package com.admtel.telephonyserver.interfaces;

public interface AddressTranslator {
	//sip:<destination>[@<gateway>]
	//iax2:<destination>[@<gateway>]
	//pstn:<destination>
	public String translate(String address);
}
