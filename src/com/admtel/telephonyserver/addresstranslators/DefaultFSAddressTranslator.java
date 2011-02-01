package com.admtel.telephonyserver.addresstranslators;

import com.admtel.telephonyserver.core.AdmAddress;
import com.admtel.telephonyserver.interfaces.AddressTranslator;

public class DefaultFSAddressTranslator implements AddressTranslator {

	@Override
	public String translate(AdmAddress address){
		String result = "";
		if (address != null){
			switch (address.getProtocol()){
			case SIP:
				if (address.getGateway() != null){
					result = String.format("sofia/internal/%s@%s", address.getDestination(), address.getGateway());
				}
				else{
					result = String.format("sofia/internal/%s", address.getDestination());
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
