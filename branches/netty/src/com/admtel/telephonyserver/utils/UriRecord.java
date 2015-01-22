package com.admtel.telephonyserver.utils;

public class UriRecord {
	public String username="";
	public String host="";
	public int port=0;
	@Override
	public String toString() {
		return "UriRecord [" + (username != null ? "username=" + username + ", " : "")
				+ (host != null ? "host=" + host + ", " : "") + "port=" + port + "]";
	}
	
}
