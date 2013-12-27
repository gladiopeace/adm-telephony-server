package com.admtel.telephonyserver.radius;

import org.tinyradius.packet.RadiusPacket;

public class RadiusPacketDecorator {
	private RadiusPacket radiusPacket;

	public RadiusPacketDecorator (RadiusPacket radiusPacket){
		this.radiusPacket = radiusPacket;
	}
	public void addAttribute(String attribute, String value){
		if (value != null && !value.isEmpty()){
			radiusPacket.addAttribute(attribute, value);
		}
	}

	
}
