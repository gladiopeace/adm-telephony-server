package com.admtel.telephonyserver.addresstranslators;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.AdmAddress;
import com.admtel.telephonyserver.interfaces.AddressTranslator;

public class DefaultFSAddressTranslator implements AddressTranslator {

	public static Logger log = Logger.getLogger(DefaultFSAddressTranslator.class);
	@Override
	public String translate(AdmAddress address){
		log.trace("Translating address " + address);
		String result = "";
		if (address != null){
			switch (address.getProtocol()){
			case SIP:
				if (address.getGateway() != null){
					if (address.getGateway().contains(".")){ //Address is a domain
						result = String.format("sofia/external/%s@%s", address.getDestination(), address.getGateway());
					}
					else{ //Address is the name of a gateway
						result = String.format("sofia/gateway/%s/%s",address.getGateway(), address.getDestination());
					}
				}
				else{
					result = String.format("sofia/internal/%s", address.getDestination()+"%");
				}
				break;
			case IAX2:
				if (address.getGateway() != null){
					result =  String.format("IAX2/%s/%s", address.getGateway(), address.getDestination());
				}
				else{
					result =  String.format("IAX2/%s", address.getDestination());
				}
				break;
			case Local:
					result = "loopback/"+address.getDestination();
				break;
			}
		}
		return result;
	}
}
