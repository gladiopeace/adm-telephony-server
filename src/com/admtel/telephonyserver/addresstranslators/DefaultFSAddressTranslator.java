package com.admtel.telephonyserver.addresstranslators;

import com.admtel.telephonyserver.interfaces.AddressTranslator;

public class DefaultFSAddressTranslator implements AddressTranslator {

	@Override
	public String translate(String address) {
		if (address.startsWith("sip:")){
			String[] addressItems = address.substring(4).split("@");
			if (addressItems.length == 2){
				return String.format("sofia/default/%s@%s", addressItems[0], addressItems[1]);
			}
			else if (addressItems.length==1){
				return String.format("sofia/internal/%s", addressItems[0]+"%");
			}
		}
		else if (address.startsWith("iax2:")){
			String[] addressItems = address.substring(5).split("@");
			if (addressItems.length == 2){
				return String.format("IAX2/%s/%s", addressItems[1], addressItems[0]);
			}
			else if (addressItems.length==1){
				return String.format("IAX2/%s", addressItems[0]);
			}
		}
		return "";
	}

}
